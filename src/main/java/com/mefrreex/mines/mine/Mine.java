package com.mefrreex.mines.mine;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import com.mefrreex.mines.Mines;
import com.mefrreex.mines.event.MineInitEvent;
import com.mefrreex.mines.event.MineUpdateEvent;
import com.mefrreex.mines.manager.MineManager;
import com.mefrreex.mines.task.UpdateMineTask;
import com.mefrreex.mines.utils.Area;
import com.mefrreex.mines.utils.Point;
import com.mefrreex.mines.utils.PointLocation;
import com.mefrreex.mines.utils.Utils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Getter @Setter
@ToString(exclude = {"main"})
public class Mine {
    
    /** Mine name */
    @SerializedName("name") private final String name;
    
    /** Area and level name */
    @SerializedName("area") private Area area;
    @SerializedName("level") private String levelName;
    
    /** Mine permission */
    @SerializedName("locked") private boolean locked;
    @SerializedName("permission") private String permission;   

    /** Auto update and update interval */
    @SerializedName("autoUpdate") private boolean autoUpdate = true;
    @SerializedName("updateInterval") private long updateInterval = 60;
    private transient AtomicLong currentUpdateInterval;

    /** Update below percent and update percen*/
    @SerializedName("updateBelowPercent") private boolean updateBelowPercent;
    @SerializedName("updatePercent") private double updatePercent = 0;

    /** Safe update */
    @SerializedName("safeUpdate") private boolean safeUpdate;
    /** Teleport point */
    @SerializedName("teleportPoint") private PointLocation teleportPoint;

    /** Mine blocks */
    @SerializedName("blocks") private List<MineBlock> blocks = new ArrayList<>();
    private transient AtomicLong currentSize;
    
    private transient boolean updating;

    @Setter(value = AccessLevel.PRIVATE)
    private transient Mines main;

    public Mine(String name) {
        this(name, null);
    }

    public Mine(String name, Area area) {
        this.name = name;
        this.area = area;
    }

    /**
     * Init mine
     * @param main
     * @return Mines instance
     */
    public boolean init(Mines main) {
        if (this.main != null) {
            throw new RuntimeException("Mine is not initialized");
        }

        MineInitEvent event = new MineInitEvent(this);
        main.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return false;
        }

        this.main = main;
        this.currentUpdateInterval = new AtomicLong(updateInterval);
        this.currentSize = new AtomicLong(0);
        MineManager.getMines().computeIfAbsent(getLevel(), 
            mines -> new ArrayList<>()).add(this);
        return true;

    }

    /**
     * Get first Point
     * @return Point
     */
    public Point getFirstPoint() {
        return area != null ? area.getMinPoint() : null;
    }

    /**
     * Set first Point
     * @param point Point
     */
    public void setFirstPoint(Point point) {
        this.area = new Area(point, this.getFirstPoint());
    }

    /**
     * Get second Point
     * @return Point
     */
    public Point getSecondPoint() {
        return area != null ? area.getMaxPoint() : null;
    }

    /**
     * Set second Point
     * @param point Point
     */
    public void setSecondPoint(Point point) {
        this.area = new Area(this.getSecondPoint(), point);
    }

    /**
     * Set mine update interval
     * @param interval Interval in seconds
     */
    public void setUpdateInterval(long interval) {
        this.updateInterval = interval;
        this.currentUpdateInterval.set(interval);
    }

    /**
     * Get mine level
     * @return
     */
    public Level getLevel() {
        return main.getServer().getLevelByName(levelName);
    }

    /**
     * Get the size of the mine
     * @return Size
     */
    public long getSize() {
        return area.getSize();
    }

    /**
     * Get mine occupancy percentages
     * @return Percent
     */
    public double getOccupancyPercent() {
        return Utils.toPercentage(currentSize.get(), 0, this.getSize());
    }

    /**
     * Get MineBlock from Nukkit block
     * @param block Block
     * @return      MineBlock
     */
    public MineBlock getMineBlock(Block block) {
        for (MineBlock mineBlock : blocks) {
            if (mineBlock.getId() == block.getId() && 
                mineBlock.getDamage() == block.getDamage()) {
                return mineBlock;
            }
        }
        return null;
    }

    /**
     * Is point in this mine
     * @param point Point
     * @return      boolean
     */
    public boolean isInMine(Point point) {
        return area.isInside(point);
    }

    /**
     * Get a random block based on the chances
     * @return MineBlock
     */
    public MineBlock getBlockWithChance() {
        return Utils.getBlockWithChance(blocks);
    }

    /**
     * Update mine blocks
     */
    public void update() {
        this.update(false);
    }

    /**
     * Update mine blocks
     * @param silent Do not call MineUpdateEvent
     */
    public void update(boolean silent) {
        if (main == null) {
            throw new RuntimeException("Mine is not initialized");
        }

        MineUpdateEvent event = new MineUpdateEvent(this);
        if (!silent) main.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        if (this.getLevel() == null || this.getLevel().getProvider() == null) return;
        
        this.currentUpdateInterval.set(updateInterval);
        this.currentSize.set(0);

        if (safeUpdate) {
            List<Player> players = Utils.getPlayersBetween(
                this.getFirstPoint(), this.getSecondPoint(), 
                this.getLevel()
            );
            for (Player player : players) {
                if (teleportPoint != null) player.teleport(teleportPoint.toLocation());
            }
        }

        UpdateMineTask task = new UpdateMineTask(this);
        main.getServer().getScheduler().scheduleAsyncTask(main, task);
    }

    /**
     * Remove mine
     * @return Is removed
     */
    public boolean remove() {
        return MineManager.remove(this);
    }
}