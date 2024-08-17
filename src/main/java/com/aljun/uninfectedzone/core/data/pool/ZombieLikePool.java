package com.aljun.uninfectedzone.core.data.pool;

import com.aljun.uninfectedzone.api.registry.UninfectedZoneRegistry;
import com.aljun.uninfectedzone.api.zombie.zombielike.ZombieLike;
import com.aljun.uninfectedzone.core.client.gui.modify.AbstractConfigModifyScreen;
import com.aljun.uninfectedzone.core.client.gui.modify.DummyModifyScreen;
import com.aljun.uninfectedzone.core.utils.ComponentUtils;
import com.aljun.uninfectedzone.core.utils.RandomHelper;
import com.aljun.uninfectedzone.core.utils.VarSet;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.PredicateManager;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ZombieLikePool extends AbstractPool<ZombieLikePool> {

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final ZombieLikePool EMPTY = new ZombieLikePool();
    private static final DeserializationContext DESERIALIZATION_CONTEXT = new DeserializationContext(new ResourceLocation("uninfectedzone:condition_deserialization_context"), new PredicateManager());

    /**
     * 请不要拿去用在 Tag 上面
     */
    public static VarSet.VarType<ZombieLikePool> ZOMBIE_LIKE_POOL = new VarSet.VarType<>("zombie_pool") {

        private static final Gson GSON = new Gson();

        @OnlyIn(Dist.CLIENT)
        @Override
        public AbstractConfigModifyScreen<ZombieLikePool> createModifyScreen(ZombieLikePool origin, VarSet<ZombieLikePool> varSet, Consumer<ZombieLikePool> consumer, MutableComponent displayName) {
            return DummyModifyScreen.create(origin, varSet, consumer, displayName);
        }

        @Override
        public String asString(ZombieLikePool zombieLikePool) {
            return "";
        }

        @Override
        public ZombieLikePool fromString(String tString) {
            return null;
        }

        @Override
        public Component asText(ZombieLikePool zombieLikePool) {
            return ComponentUtils.literature("...");
        }

        @Contract(pure = true)
        @Deprecated
        @Override
        public @Nullable Tag writeToTag(ZombieLikePool zombieLikePool) {
            return null;
        }

        @Contract(pure = true)
        @Deprecated
        @Override
        public @Nullable ZombieLikePool readFromTag(Tag tag) {
            return null;
        }

        @Override
        public ZombieLikePool deserialize(JsonObject jsonObject, String key) {
            if (jsonObject.has(key)) {
                if (jsonObject.get(key).isJsonObject()) {
                    JsonObject poolJson = jsonObject.getAsJsonObject(key);
                    return ZombieLikePool.fromJson(poolJson);
                }
            }
            return EMPTY;
        }

        @Override
        public void serialize(JsonObject jsonObject, String key, ZombieLikePool zombieLikePool) {
            jsonObject.add(key, zombieLikePool.toJson());
        }
    };
    private final Gson predicateGson = Deserializers.createConditionSerializer().create();
    private final HashMap<Integer, Double> POOL_WEIGHT;
    private final HashMap<Integer, List<LootItemCondition>> POOL_CONDITIONS;
    private final HashMap<Integer, ResourceLocation> POOL_VALUE;
    private final RandomHelper.RandomPool<String> randomPool = RandomHelper.RandomPool.builder(String.class).build();
    private JsonObject conditionsJson = new JsonObject();

    private ZombieLikePool(JsonObject jsonObject) {
        this();
        if (jsonObject.has("candidates")) {
            if (jsonObject.get("candidates").isJsonArray()) {
                JsonArray candidates = jsonObject.getAsJsonArray("candidates");
                candidates.forEach(jsonElement -> {
                    if (jsonElement.isJsonObject()) {
                        int i = POOL_VALUE.size();
                        JsonObject candidate = jsonElement.getAsJsonObject();
                        String zombieLike;
                        if (candidate.has("type")) {
                            if (candidate.get("type").isJsonPrimitive()) {
                                zombieLike = candidate.get("type").getAsString();
                            } else {
                                LOGGER.error("Loading ZombieLike Pool failed : \"type\" illegal\n{}", candidate);
                                return;
                            }
                        } else {
                            LOGGER.error("Loading ZombieLike Pool failed : \"type\" absent\n{}", candidate);
                            return;
                        }

                        double weight;
                        if (candidate.has("weight")) {
                            if (candidate.get("weight").isJsonPrimitive()) {
                                weight = candidate.get("weight").getAsDouble();
                                if (weight <= 0d) {
                                    LOGGER.error("Loading ZombieLike Pool failed : \"weight\" illegal (must > 0, but : {})\n{}", weight, candidate);
                                    return;
                                }
                            } else {
                                LOGGER.error("Loading ZombieLike Pool failed : \"weight\" illegal\n{}", candidate);
                                return;
                            }
                        } else {
                            LOGGER.error("Loading ZombieLike Pool failed : \"weight\" absent\n{}", candidate);
                            return;
                        }

                        List<LootItemCondition> conditionList = new ArrayList<>();
                        if (candidate.has("conditions")) {
                            if (candidate.get("conditions").isJsonArray()) {
                                JsonArray conditionsArray = candidate.getAsJsonArray("conditions");
                                conditionsArray.forEach(jsonElement1 -> {
                                    try {
                                        conditionList.add(predicateGson.fromJson(jsonElement1, LootItemCondition.class));
                                    } catch (Throwable throwable) {
                                        LOGGER.error("Loading Conditions(\n{}\n) failed : {}", jsonElement1, throwable.toString());
                                    }
                                });
                            }
                        }

                        POOL_VALUE.put(i, new ResourceLocation(zombieLike));
                        POOL_WEIGHT.put(i, weight);
                        POOL_CONDITIONS.put(i, conditionList);
                    }
                });
            }
        }
    }

    public ZombieLikePool() {
        this.POOL_VALUE = new HashMap<>();
        this.POOL_WEIGHT = new HashMap<>();
        this.POOL_CONDITIONS = new HashMap<>();
    }

    private static ZombieLikePool fromJson(JsonObject jsonObject) {
        return new ZombieLikePool(jsonObject);
    }

    public ZombieLikePool add(ZombieLike zombieLike, double weight, String conditions) {
        if (weight > 0) {
            JsonObject object = predicateGson.fromJson(conditions, JsonObject.class);
            add(zombieLike, weight, object);
        }
        return this;
    }

    public ZombieLikePool add(ZombieLike zombieLike, double weight, JsonObject conditions) {
        if (weight > 0) {

            JsonArray conditionsArray = conditions.getAsJsonArray("conditions");
            List<LootItemCondition> conditionList = new ArrayList<>();
            conditionsArray.forEach(jsonElement -> {
                try {
                    conditionList.add(predicateGson.fromJson(jsonElement, LootItemCondition.class));
                } catch (Throwable throwable) {
                    LOGGER.error("Adding Conditions(\n{}\n) failed : {}", jsonElement, throwable.toString());
                }
            });
            this.add(zombieLike, weight, conditionList);
        }
        return this;
    }

    public ZombieLikePool add(ZombieLike zombieLike, double weight, List<LootItemCondition> conditionList) {
        if (weight > 0) {
            int id = this.POOL_VALUE.size();
            this.POOL_VALUE.put(id, zombieLike.getRegistryName());
            this.POOL_WEIGHT.put(id, weight);
            this.POOL_CONDITIONS.put(id, conditionList);
        }
        return this;
    }

    public ZombieLikePool add(ZombieLike zombieLike, double weight, LootItemCondition... conditions) {
        if (weight > 0) {
            this.add(zombieLike, weight, List.of(conditions));
        }
        return this;
    }

    private JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        JsonArray candidates = new JsonArray();
        this.POOL_VALUE.forEach((key, value) -> {
            JsonObject candidate = new JsonObject();
            candidate.addProperty("type", value.toString());
            candidate.addProperty("weight", this.POOL_WEIGHT.get(key));
            List<LootItemCondition> conditionList = this.POOL_CONDITIONS.get(key);
            JsonArray conditions = new JsonArray();
            if (!conditionList.isEmpty()) {
                conditionList.forEach(condition -> {
                    try {
                        JsonElement conditionJson = conditionToJson(condition);
                        conditions.add(conditionJson);
                    } catch (Throwable throwable) {
                        LOGGER.error("Saving Conditions(\n{}\n) failed : {}", conditionList, throwable.toString());
                    }
                });
            }
            candidate.add("conditions", conditions);
            candidates.add(candidate);
        });
        jsonObject.add("candidates", candidates);
        return jsonObject;
    }

    private JsonElement conditionToJson(LootItemCondition condition) {
        return predicateGson.toJsonTree(conditionsJson);
    }

    public ZombieLike nextValue(ServerLevel serverLevel, @NotNull Mob mob) {

        List<Integer> legalList = new ArrayList<>();

        LootContext context = new LootContext.Builder(serverLevel)
                .withRandom(mob.getRandom())
                .withParameter(LootContextParams.THIS_ENTITY, mob)
                .withParameter(LootContextParams.ORIGIN, mob.position()).create(LootContextParamSets.ENTITY);


        this.POOL_CONDITIONS.forEach((key, conditions) -> {
            final boolean[] b = {true};
            conditions.forEach(condition -> {
                if (!b[0]) return;
                b[0] = condition.test(context);
            });
            if (b[0]) {
                legalList.add(key);
            }
        });

        ResourceLocation id = null;

        final double[] weightTotal = {0d};
        legalList.forEach((key) -> {
            this.POOL_WEIGHT.forEach((key2, weight) -> {
                if (Objects.equals(key, key2)) weightTotal[0] += weight;
            });
        });
        double random = RandomHelper.RANDOM.nextDouble(0d, weightTotal[0]);
        double before = 0d;
        double after = 0d;
        int i = -1;

        for (double j : legalList) {
            i++;
            after += j;
            if (before <= random && random <= after) id = this.POOL_VALUE.get(i);
            before += j;
        }

        if (id != null) {
            ZombieLike zombieLike = UninfectedZoneRegistry.ZOMBIE_LIKES.get().getValue(id);
            if (zombieLike != null) {
                return zombieLike;
            }
        }

        return ZombieLike.DUMMY.get();
    }
}
