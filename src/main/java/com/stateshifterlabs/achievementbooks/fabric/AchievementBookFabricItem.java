package com.stateshifterlabs.achievementbooks.fabric;
import com.stateshifterlabs.achievementbooks.core.data.Book;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class AchievementBookFabricItem extends Item {

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
}
