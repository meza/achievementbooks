package com.stateshifterlabs.achievementbooks.minecraftdependent;

import com.stateshifterlabs.achievementbooks.core.data.AchievementData;
import com.stateshifterlabs.achievementbooks.core.data.Book;
import com.stateshifterlabs.achievementbooks.minecraftdependent.UI.BookScreen;
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

public class AchievementBookItem extends Item {
    private static final Logger LOGGER = LogManager.getLogger(AchievementBookItem.class);
    private final Book book;

    public AchievementBookItem(Book book) {

        super(new Settings()
                .group(ItemGroup.MISC)
                .maxCount(1)
                .maxDamage(0)
                .fireproof()
        );
        this.book = book;
    }

    public String colour() {
        return this.book.colour();
    }

    public void updateBook(AchievementData newAchievementData) {
        this.book.loadDone(newAchievementData.completed(this.book.itemName()));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        LOGGER.debug("Opening the book: " + book.itemName());
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

    @Override
    public Text getName(ItemStack item) {
        return new LiteralText(this.book.name());
    }
}
