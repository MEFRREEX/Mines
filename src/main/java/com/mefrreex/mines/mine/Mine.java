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
@ToString
public class Mine {
    
    @SerializedName("name") private final String name;
    
    @SerializedName("area") private Area area;
    @SerializedName("level") private String levelName;
    
    @SerializedName("autoUpdate") private boolean autoUpdate = true;
    @SerializedName("updateInterval") private long updateInterval = 60;
    private transient AtomicLong currentUpdateInterval;

    @SerializedName("updateBelowPercent") private boolean updateBelowPercent;
    @SerializedName("updatePercent") private double updatePercent = 0;

    @SerializedName("safeUpdate") private boolean safeUpdate;
    @SerializedName("teleportPoint") private PointLocation teleportPoint;

    @SerializedName("blocks") private List<MineBlock> blocks = new ArrayList<>();
    private transient AtomicLong currentSize;

    @SerializedName("locked") private boolean locked;
    @SerializedName("permission") private String permission;   
    
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

    public boolean init(Mines main) {
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

    public Point getFirstPoint() {
        return area != null ? area.getMinPoint() : null;
    }

    public void setFirstPoint(Point point) {
        this.area = new Area(point, area.getMaxPoint());
    }

    public Point getSecondPoint() {
        return area != null ? area.getMaxPoint() : null;
    }

    public void setSecondPoint(Point point) {
        this.area = new Area(area.getMinPoint(), point);
    }

    public void setUpdateInterval(long delay) {
        this.updateInterval = delay;
        this.currentUpdateInterval.set(delay);
    }

    public Level getLevel() {
        return main.getServer().getLevelByName(levelName);
    }

    public long getSize() {
        return area.getSize();
    }

    public double getOccupancyPercent() {
        return Utils.toPercentage(currentSize.get(), 0, this.getSize());
    }

    public MineBlock getMineBlock(Block block) {
        for (MineBlock mineBlock : blocks) {
            if (mineBlock.getId() == block.getId() && 
                mineBlock.getDamage() == block.getDamage()) {
                return mineBlock;
            }
        }
        return null;
    }

    public boolean isInMine(Point point) {
        return area.isInside(point);
    }

    public MineBlock getBlockWithChance() {
        return Utils.getBlockWithChance(blocks);
    }

    public void update() {
        this.update(false);
    }

    public void update(boolean silent) {
        if (main == null) {
            throw new RuntimeException("Mine is not initialized");
        }

        MineUpdateEvent event = new MineUpdateEvent(this);
        if (!silent) main.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        if (this.getLevel() == null) return;
        if (this.getLevel().getProvider() == null) return;
        
        this.updating = true;
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
        main.getServer().getScheduler().scheduleTask(main, task);
    }

    public boolean remove() {
        return MineManager.remove(this);
    }
}