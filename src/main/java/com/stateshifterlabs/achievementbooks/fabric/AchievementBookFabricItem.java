package com.stateshifterlabs.achievementbooks.fabric;

import com.stateshifterlabs.achievementbooks.core.data.AchievementData;
import com.stateshifterlabs.achievementbooks.core.data.Book;
import com.stateshifterlabs.achievementbooks.fabric.UI.BookScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AchievementBookFabricItem extends Item {
    private static final Logger LOGGER = LogManager.getLogger(AchievementBookFabricItem.class);
    private Book book;

    public AchievementBookFabricItem(Book book) {
        super(new FabricItemSettings()
                .group(ItemGroup.MISC)
                .maxCount(1)
                .maxDamage(0)
                .fireproof()
        );
        this.book = book;
    }

    @Override
    public Text getName(ItemStack item) {
        return new LiteralText(this.book.name());
    }

    @Override
    @Environment(EnvType.CLIENT)
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        LOGGER.info("Opening the book: "+book.itemName());
        ItemStack stackInHand = playerEntity.getStackInHand(hand);
        if (world.isClient) {
            MinecraftClient.getInstance().setScreen(new BookScreen(
                    this.book,
                    world,
                    playerEntity
            ));
        }
        return TypedActionResult.success(stackInHand);
    }

    public String colour() {
        return this.book.colour();
    }
    public void updateBook(AchievementData newAchievementData) {
        this.book.loadDone(newAchievementData.completed(this.book.itemName()));
    }
}
