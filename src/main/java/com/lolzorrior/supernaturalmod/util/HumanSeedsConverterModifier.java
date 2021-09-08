package com.lolzorrior.supernaturalmod.util;

import com.google.gson.JsonObject;
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
    public HumanSeedsConverterModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        generatedLoot.add(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:seeds"))));
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<HumanSeedsConverterModifier> {

        @Override
        public HumanSeedsConverterModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
            String type = object.get("type").getAsString();
            //Item seed = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(object, "item")));
            return new HumanSeedsConverterModifier(ailootcondition);
        }

        @Override
        public JsonObject write(HumanSeedsConverterModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            //json.addProperty("item", ForgeRegistries.ITEMS.getValue(instance.reward.getRegistryName()).toString());
            return json;
        }
    }
}


