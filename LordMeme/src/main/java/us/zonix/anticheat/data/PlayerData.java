package us.zonix.anticheat.data;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.entity.Player;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.check.ICheck;
import us.zonix.anticheat.check.impl.aimassist.AimAssistA;
import us.zonix.anticheat.check.impl.aimassist.AimAssistB;
import us.zonix.anticheat.check.impl.aimassist.AimAssistC;
import us.zonix.anticheat.check.impl.aimassist.AimAssistD;
import us.zonix.anticheat.check.impl.aimassist.AimAssistE;
import us.zonix.anticheat.check.impl.autoclicker.AutoClickerA;
import us.zonix.anticheat.check.impl.autoclicker.AutoClickerB;
import us.zonix.anticheat.check.impl.autoclicker.AutoClickerC;
import us.zonix.anticheat.check.impl.autoclicker.AutoClickerD;
import us.zonix.anticheat.check.impl.autoclicker.AutoClickerE;
import us.zonix.anticheat.check.impl.autoclicker.AutoClickerF;
import us.zonix.anticheat.check.impl.autoclicker.AutoClickerG;
import us.zonix.anticheat.check.impl.autoclicker.AutoClickerH;
import us.zonix.anticheat.check.impl.autoclicker.AutoClickerI;
import us.zonix.anticheat.check.impl.autoclicker.AutoClickerJ;
import us.zonix.anticheat.check.impl.autoclicker.AutoClickerK;
import us.zonix.anticheat.check.impl.autoclicker.AutoClickerL;
import us.zonix.anticheat.check.impl.badpackets.BadPacketsA;
import us.zonix.anticheat.check.impl.badpackets.BadPacketsB;
import us.zonix.anticheat.check.impl.badpackets.BadPacketsC;
import us.zonix.anticheat.check.impl.badpackets.BadPacketsD;
import us.zonix.anticheat.check.impl.badpackets.BadPacketsE;
import us.zonix.anticheat.check.impl.badpackets.BadPacketsF;
import us.zonix.anticheat.check.impl.badpackets.BadPacketsG;
import us.zonix.anticheat.check.impl.badpackets.BadPacketsH;
import us.zonix.anticheat.check.impl.badpackets.BadPacketsI;
import us.zonix.anticheat.check.impl.badpackets.BadPacketsJ;
import us.zonix.anticheat.check.impl.badpackets.BadPacketsK;
import us.zonix.anticheat.check.impl.badpackets.BadPacketsL;
import us.zonix.anticheat.check.impl.fly.FlyA;
import us.zonix.anticheat.check.impl.fly.FlyB;
import us.zonix.anticheat.check.impl.fly.FlyC;
import us.zonix.anticheat.check.impl.inventory.InventoryA;
import us.zonix.anticheat.check.impl.inventory.InventoryB;
import us.zonix.anticheat.check.impl.inventory.InventoryC;
import us.zonix.anticheat.check.impl.inventory.InventoryD;
import us.zonix.anticheat.check.impl.inventory.InventoryE;
import us.zonix.anticheat.check.impl.inventory.InventoryF;
import us.zonix.anticheat.check.impl.inventory.InventoryG;
import us.zonix.anticheat.check.impl.killaura.KillAuraA;
import us.zonix.anticheat.check.impl.killaura.KillAuraB;
import us.zonix.anticheat.check.impl.killaura.KillAuraC;
import us.zonix.anticheat.check.impl.killaura.KillAuraD;
import us.zonix.anticheat.check.impl.killaura.KillAuraE;
import us.zonix.anticheat.check.impl.killaura.KillAuraF;
import us.zonix.anticheat.check.impl.killaura.KillAuraG;
import us.zonix.anticheat.check.impl.killaura.KillAuraH;
import us.zonix.anticheat.check.impl.killaura.KillAuraI;
import us.zonix.anticheat.check.impl.killaura.KillAuraJ;
import us.zonix.anticheat.check.impl.killaura.KillAuraK;
import us.zonix.anticheat.check.impl.killaura.KillAuraL;
import us.zonix.anticheat.check.impl.killaura.KillAuraM;
import us.zonix.anticheat.check.impl.killaura.KillAuraN;
import us.zonix.anticheat.check.impl.killaura.KillAuraO;
import us.zonix.anticheat.check.impl.killaura.KillAuraP;
import us.zonix.anticheat.check.impl.killaura.KillAuraQ;
import us.zonix.anticheat.check.impl.killaura.KillAuraR;
import us.zonix.anticheat.check.impl.killaura.KillAuraS;
import us.zonix.anticheat.check.impl.range.RangeA;
import us.zonix.anticheat.check.impl.scaffold.ScaffoldA;
import us.zonix.anticheat.check.impl.scaffold.ScaffoldB;
import us.zonix.anticheat.check.impl.scaffold.ScaffoldC;
import us.zonix.anticheat.check.impl.step.StepA;
import us.zonix.anticheat.check.impl.timer.TimerA;
import us.zonix.anticheat.check.impl.velocity.VelocityA;
import us.zonix.anticheat.check.impl.velocity.VelocityB;
import us.zonix.anticheat.check.impl.velocity.VelocityC;
import us.zonix.anticheat.check.impl.wtap.WTapA;
import us.zonix.anticheat.check.impl.wtap.WTapB;
import us.zonix.anticheat.client.EnumClientType;
import us.zonix.anticheat.util.CustomLocation;

@Setter @Getter public final class PlayerData {

	private static final Map<Class<? extends ICheck>, Constructor<? extends ICheck>> CONSTRUCTORS;
	public static final Class<? extends ICheck>[] CHECKS;

	private final Map<UUID, List<CustomLocation>> recentPlayerPackets;
	private final Map<ICheck, Set<Long>> checkViolationTimes;

	private final Map<Class<? extends ICheck>, ICheck> checkMap;
	private final Map<Integer, Long> keepAliveTimes;
	private final Map<ICheck, Double> checkVlMap;

	private final Set<BlockPosition> fakeBlocks = new HashSet<>();

	private final Set<UUID> playersWatching;
	private final Set<String> filteredPhrases;
	private final Set<String> phrasesListeningTo;
	private final Set<CustomLocation> teleportLocations;
	private Map<String, String> forgeMods;
	private StringBuilder sniffedPacketBuilder;
	private CustomLocation lastMovePacket;
	private EnumClientType client;
	private UUID lastTarget;
	private String randomBanReason;
	private double randomBanRate;
	private double misplace;
	private boolean randomBan;
	private boolean allowTeleport;
	private boolean inventoryOpen;
	private boolean setInventoryOpen;
	private boolean sendingVape;
	private boolean attackedSinceVelocity;
	private boolean underBlock;
	private boolean sprinting;
	private boolean inLiquid;
	private boolean instantBreakDigging;
	private boolean fakeDigging;
	private boolean onGround;
	private boolean sniffing;
	private boolean onStairs;
	private boolean onCarpet;
	private boolean banWave;
	private boolean placing;
	private boolean banning;
	private boolean digging;
	private boolean inWeb;
	private boolean onIce;
	private boolean wasUnderBlock;
	private boolean wasOnGround;
	private boolean wasInLiquid;
	private boolean wasInWeb;
	private double lastGroundY;
	private double velocityX;
	private double velocityY;
	private double velocityZ;
	private long lastDelayedMovePacket;
	private long lastAnimationPacket;
	private long lastAttackPacket;
	private long lastVelocity;
	private long ping;
	private int velocityH;
	private int velocityV;
	private int lastCps;
	private int movementsSinceIce;
	private int movementsSinceUnderBlock;

	public PlayerData(final LordMeme plugin) {
		this.recentPlayerPackets = new HashMap<>();
		this.checkViolationTimes = new HashMap<>();
		this.checkMap = new HashMap<>();
		this.keepAliveTimes = new HashMap<>();
		this.checkVlMap = new HashMap<>();
		this.playersWatching = new HashSet<>();
		this.filteredPhrases = new HashSet<>();
		this.phrasesListeningTo = new HashSet<>();
		this.teleportLocations = Collections.newSetFromMap(new ConcurrentHashMap<CustomLocation, Boolean>());
		this.sniffedPacketBuilder = new StringBuilder();
		this.client = EnumClientType.VANILLA;

		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () ->
				PlayerData.CONSTRUCTORS.keySet().stream().map(o -> (Class<? extends ICheck>) o).forEach(check -> {
					Constructor<? extends ICheck> constructor = PlayerData.CONSTRUCTORS.get(check);
					try {
						this.checkMap.put(check, constructor.newInstance(plugin, this));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}));
	}

	@SuppressWarnings("unchecked") public <T extends ICheck> T getCheck(final Class<T> clazz) {
		return (T) this.checkMap.get(clazz);
	}

	public CustomLocation getLastPlayerPacket(final UUID playerUUID, final int index) {
		final List<CustomLocation> customLocations = this.recentPlayerPackets.get(playerUUID);
		if (customLocations != null && customLocations.size() > index) {
			return customLocations.get(customLocations.size() - index);
		}
		return null;
	}

	public void addPlayerPacket(final UUID playerUUID, final CustomLocation customLocation) {
		List<CustomLocation> customLocations = this.recentPlayerPackets.get(playerUUID);
		if (customLocations == null) {
			customLocations = new ArrayList<>();
		}
		if (customLocations.size() == 20) {
			customLocations.remove(0);
		}
		customLocations.add(customLocation);
		this.recentPlayerPackets.put(playerUUID, customLocations);
	}

	public void addTeleportLocation(final CustomLocation teleportLocation) {
		this.teleportLocations.add(teleportLocation);
	}

	public boolean allowTeleport(final CustomLocation teleportLocation) {
		for (final CustomLocation customLocation : this.teleportLocations) {
			final double delta = Math.pow(teleportLocation.getX() - customLocation.getX(), 2.0) +
			                     Math.pow(teleportLocation.getZ() - customLocation.getZ(), 2.0);
			if (delta <= 0.005) {
				this.teleportLocations.remove(customLocation);
				return true;
			}
		}
		return false;
	}

	public double getCheckVl(final ICheck check) {
		if (!this.checkVlMap.containsKey(check)) {
			this.checkVlMap.put(check, 0.0);
		}
		return this.checkVlMap.get(check);
	}

	public void setCheckVl(double vl, final ICheck check) {
		if (vl < 0.0) {
			vl = 0.0;
		}
		this.checkVlMap.put(check, vl);
	}

	public boolean isPlayerWatching(final Player player) {
		return this.playersWatching.contains(player.getUniqueId());
	}

	public void togglePlayerWatching(final Player player) {
		if (!this.playersWatching.remove(player.getUniqueId())) {
			this.playersWatching.add(player.getUniqueId());
		}
	}

	public boolean isPhraseFiltered(final String phrase) {
		return this.filteredPhrases.contains(phrase);
	}

	public void togglePhraseFilter(final String phrase) {
		if (!this.filteredPhrases.remove(phrase)) {
			this.filteredPhrases.add(phrase);
		}
	}

	public boolean isPhraseBeingListenedTo(final String phrase) {
		return this.phrasesListeningTo.contains(phrase);
	}

	public void toggleListeningPhrase(final String phrase) {
		if (!this.phrasesListeningTo.remove(phrase)) {
			this.phrasesListeningTo.add(phrase);
		}
	}

	public boolean areAnyPhrasesBeingListenedTo() {
		return this.phrasesListeningTo.size() > 0;
	}

	public boolean keepAliveExists(final int id) {
		return this.keepAliveTimes.containsKey(id);
	}

	public long getKeepAliveTime(final int id) {
		return this.keepAliveTimes.get(id);
	}

	public void removeKeepAliveTime(final int id) {
		this.keepAliveTimes.remove(id);
	}

	public void addKeepAliveTime(final int id) {
		this.keepAliveTimes.put(id, System.currentTimeMillis());
	}

	public int getViolations(final ICheck check, final Long time) {
		final Set<Long> timestamps = this.checkViolationTimes.get(check);
		if (timestamps != null) {
			int violations = 0;
			for (final long timestamp : timestamps) {
				if (System.currentTimeMillis() - timestamp <= time) {
					++violations;
				}
			}
			return violations;
		}
		return 0;
	}

	public void addViolation(final ICheck check) {
		Set<Long> timestamps = this.checkViolationTimes.get(check);
		if (timestamps == null) {
			timestamps = new HashSet<>();
		}
		timestamps.add(System.currentTimeMillis());
		this.checkViolationTimes.put(check, timestamps);
	}

	static {
		//noinspection unchecked
		CHECKS = new Class[]{
				AimAssistA.class, AimAssistB.class, AimAssistC.class, AimAssistD.class,
				AimAssistE.class,

				AutoClickerA.class, AutoClickerB.class, AutoClickerC.class, AutoClickerD.class,
				AutoClickerE.class, AutoClickerF.class, AutoClickerG.class, AutoClickerH.class,
				AutoClickerI.class, AutoClickerJ.class, AutoClickerK.class, AutoClickerK.class,
				AutoClickerL.class,

				BadPacketsA.class, BadPacketsB.class, BadPacketsC.class, BadPacketsD.class,
				BadPacketsE.class, BadPacketsF.class, BadPacketsG.class, BadPacketsH.class,
				BadPacketsI.class, BadPacketsJ.class, BadPacketsK.class, BadPacketsL.class,

				FlyA.class, FlyB.class, FlyC.class,

				InventoryA.class, InventoryB.class, InventoryC.class, InventoryD.class,
				InventoryE.class, InventoryF.class, InventoryG.class,

				KillAuraA.class, KillAuraB.class, KillAuraC.class, KillAuraD.class,
				KillAuraE.class, KillAuraF.class, KillAuraG.class, KillAuraH.class,
				KillAuraI.class, KillAuraJ.class, KillAuraK.class, KillAuraL.class,
				KillAuraM.class, KillAuraN.class, KillAuraO.class, KillAuraP.class,
				KillAuraQ.class, KillAuraR.class, KillAuraS.class,

				RangeA.class,

				TimerA.class,

				VelocityA.class, VelocityB.class, VelocityC.class,

				WTapA.class, WTapB.class,

				ScaffoldA.class, ScaffoldB.class, ScaffoldC.class,

				StepA.class,

				/*PhaseA.class, PhaseB.class, VClipA.class, VClipB.class, StepA.class*/
		};
		CONSTRUCTORS = new ConcurrentHashMap<>();
		for (final Class<? extends ICheck> check : PlayerData.CHECKS) {
			try {
				PlayerData.CONSTRUCTORS.put(check, check.getConstructor(LordMeme.class, PlayerData.class));
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
	}
}
