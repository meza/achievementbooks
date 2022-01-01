package com.stateshifterlabs.achievementbooks.fabric;

import com.stateshifterlabs.achievementbooks.core.data.AchievementData;
import com.stateshifterlabs.achievementbooks.core.data.AchievementStorage;
import com.stateshifterlabs.achievementbooks.core.data.Book;
import com.stateshifterlabs.achievementbooks.fabric.UI.BookScreen;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class AchievementBookFabricItem extends Item {

    private final Book book;
    private final AchievementStorage achievementStorage;

    public AchievementBookFabricItem(Book book, AchievementStorage achievementStorage) {
        super(new FabricItemSettings()
                .group(ItemGroup.MISC)
                .maxCount(1)
                .maxDamage(0)
                .fireproof()
        );
        this.book = book;
        this.achievementStorage = achievementStorage;

    }

    @Override
    public Text getName(ItemStack item) {
        return new LiteralText(this.book.name());
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {

        if (world.isClient) {
            AchievementData achievementData = achievementStorage.forPlayer(playerEntity.getName().asString());
            MinecraftClient.getInstance().setScreen(new BookScreen(this.book, achievementData, world, playerEntity));
        }
        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }

    public String colour() {
        return this.book.colour();
    }
}
