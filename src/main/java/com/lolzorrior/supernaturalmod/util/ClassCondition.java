package com.lolzorrior.supernaturalmod.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;

import static com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass.SCLASS;

public class ClassCondition implements LootItemCondition {
    final LootContext.EntityTarget enT;

    ClassCondition(LootContext.EntityTarget enin) {
        this.enT = enin;
    }

    @Override
    public LootItemConditionType getType() {
        return LootItemConditions.ENTITY_PROPERTIES;
    }

    @Override
    public boolean test(LootContext lootContext) {
        String cclass = lootContext.getParamOrNull(this.enT.getParam()).getCapability(SCLASS).orElseThrow(NullPointerException::new).getSupernaturalClass();
        return cclass.equalsIgnoreCase("Human");
    }

    public static LootItemCondition.Builder entityPresent(LootContext.EntityTarget p_81863_) {
        return hasProperties(p_81863_);
    }

    public static LootItemCondition.Builder hasProperties(LootContext.EntityTarget p_81865_) {
        return () -> {
            return new ClassCondition(p_81865_);
        };
    }
    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ClassCondition> {

        @Override
        public void serialize(JsonObject p_79325_, ClassCondition p_79326_, JsonSerializationContext p_79327_) {
            p_79325_.add("entity", p_79327_.serialize(p_79326_.enT));
        }

        @Override
        public ClassCondition deserialize(JsonObject p_79323_, JsonDeserializationContext p_79324_) {
            LootContext.EntityTarget eenT = GsonHelper.getAsObject(p_79323_, "entity", p_79324_, LootContext.EntityTarget.class);
            return new ClassCondition(eenT);
        }
    }
}
