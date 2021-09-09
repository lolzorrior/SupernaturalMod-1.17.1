package com.lolzorrior.supernaturalmod.util;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.util.GsonHelper;
import org.lwjgl.system.CallbackI;

import javax.annotation.Nonnull;
import java.util.List;

public class HumanSeedsConverterModifier extends LootModifier {
    private final Item drop;
    public HumanSeedsConverterModifier(LootItemCondition[] conditionsIn, Item itemIn) {
        super(conditionsIn);
        drop = itemIn;
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        generatedLoot.add(new ItemStack(drop));
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<HumanSeedsConverterModifier> {

        @Override
        public HumanSeedsConverterModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
            String type = object.get("type").getAsString();
            Item reward = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(object, "item")));
            return new HumanSeedsConverterModifier(ailootcondition, reward);
        }

        @Override
        public JsonObject write(HumanSeedsConverterModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.addProperty("item", ForgeRegistries.ITEMS.getValue(instance.drop.getRegistryName()).toString());
            return json;
        }
    }
}


