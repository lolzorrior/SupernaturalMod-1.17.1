package com.lolzorrior.supernaturalmod.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.lolzorrior.supernaturalmod.SupernaturalMod;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.phys.Vec3;

import static com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass.SCLASS;

public class ClassCondition implements LootItemCondition {
    final EntityPredicate predicate;
    final String sclass;

    ClassCondition(EntityPredicate epin, String classIn) {
        this.predicate = epin;
        this.sclass = classIn;
    }

    @Override
    public LootItemConditionType getType() {
        return SupernaturalMod.SUPERNATURAL_PROPERTIES;
    }

    @Override
    public boolean test(LootContext lootContext) {
        String string = lootContext.getParamOrNull(LootContextParams.THIS_ENTITY).getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass();
        return string != null && string.equalsIgnoreCase("human");
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ClassCondition> {

        @Override
        public void serialize(JsonObject p_79325_, ClassCondition p_79326_, JsonSerializationContext p_79327_) {
            p_79325_.add("predicate", p_79326_.predicate.serializeToJson());
            p_79325_.add("class", p_79327_.serialize(p_79326_.sclass));
        }

        @Override
        public ClassCondition deserialize(JsonObject p_79323_, JsonDeserializationContext p_79324_) {
            EntityPredicate entityPredicate = EntityPredicate.fromJson(p_79323_.get("predicate"));
            return new ClassCondition(entityPredicate, GsonHelper.getAsString(p_79323_, "class"));
        }
    }
}
