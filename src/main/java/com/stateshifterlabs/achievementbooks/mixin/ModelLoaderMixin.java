package com.stateshifterlabs.achievementbooks.mixin;

import com.stateshifterlabs.achievementbooks.AchievementBooks;
import com.stateshifterlabs.achievementbooks.fabric.AchievementBookFabricItem;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {
    @Inject(method = "loadModelFromJson", at = @At(value = "HEAD"), cancellable = true)
    public void loadModelFromJson(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> cir) {
        //First, we check if the current item model that is being registered is from our mod. If it isn't, we continue.
        if (!"achievementbooks".equals(id.getNamespace())) return;
        String bookName = id.getPath().replace("item/", "");
        AchievementBookFabricItem book = (AchievementBookFabricItem) Registries.ITEM.get(new Identifier(AchievementBooks.MODID, bookName));

        String modelJson = createItemModelJson(book.colour());

        JsonUnbakedModel model = JsonUnbakedModel.deserialize(modelJson);
        model.id = id.toString();
        cir.setReturnValue(model);
    }

    private String createItemModelJson(String colour) {
        return "{\n" +
                "\t\"parent\": \"item/book\",\n" +
                "\t\"textures\": {\n" +
                "\t\t\"layer0\": \"" + AchievementBooks.MODID + ":item/book-" + colour + "\"\n" +
                "\t}\n" +
                "}\n";
    }
}
