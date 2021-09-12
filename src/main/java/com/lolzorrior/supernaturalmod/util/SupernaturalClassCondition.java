package com.lolzorrior.supernaturalmod.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;

import static com.lolzorrior.supernaturalmod.capabilities.SupernaturalClass.SCLASS;

public class SupernaturalClassCondition implements LootItemCondition {
    final LootContext.EntityTarget enT;
    final String sclass;

    SupernaturalClassCondition(LootContext.EntityTarget enin, String sclassIn) {
        this.enT = enin;
        this.sclass = sclassIn;
    }

    @Override
    public LootItemConditionType getType() {
        return LootItemConditions.ENTITY_PROPERTIES;
    }

    @Override
    public boolean test(LootContext lootContext) {
        String cclass = lootContext.getParamOrNull(this.enT.getParam()).getCapability(SCLASS).orElseThrow(NullPointerException::new).getsClass();
        return cclass.equalsIgnoreCase(sclass);
    }


    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<SupernaturalClassCondition> {

        @Override
        public void serialize(JsonObject p_79325_, SupernaturalClassCondition p_79326_, JsonSerializationContext p_79327_) {
            p_79325_.add("entity", p_79327_.serialize(p_79326_.enT));
            p_79325_.addProperty("class", p_79326_.sclass);
        }

        @Override
        public SupernaturalClassCondition deserialize(JsonObject p_79323_, JsonDeserializationContext p_79324_) {
            LootContext.EntityTarget eenT = GsonHelper.getAsObject(p_79323_, "entity", p_79324_, LootContext.EntityTarget.class);
            String ssclass = GsonHelper.getAsString(p_79323_, "class");
            return new SupernaturalClassCondition(eenT, ssclass);
        }
    }
}
