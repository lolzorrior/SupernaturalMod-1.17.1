package com.lolzorrior.supernaturalmod.util;

import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class HumanSeedsConverterModifier extends LootModifier {
    private final Item reward;
    public HumanSeedsConverterModifier(LootItemCondition[] conditionsIn, Item rewardIn) {
        super(conditionsIn);
        reward = rewardIn;
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        return generatedLoot;
    }
    public static class Serializer extends GlobalLootModifierSerializer<HumanSeedsConverterModifier> {

        @Override
        public HumanSeedsConverterModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
            Item seed = ForgeRegistries.ITEMS.getValue(new ResourceLocation(object.getAsString()));
            return new HumanSeedsConverterModifier(ailootcondition, seed);
        }

        @Override
        public JsonObject write(HumanSeedsConverterModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.addProperty("seedItem", ForgeRegistries.ITEMS.getKey(instance.reward).toString());
            return json;
        }
    }
}


