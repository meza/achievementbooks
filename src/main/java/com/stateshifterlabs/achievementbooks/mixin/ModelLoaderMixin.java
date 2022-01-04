package com.stateshifterlabs.achievementbooks.mixin;

import com.stateshifterlabs.achievementbooks.AchievementBooks;
import com.stateshifterlabs.achievementbooks.minecraftdependent.AchievementBookItem;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {
    @Inject(method = "loadModelFromJson", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourceManager;getResource(Lnet/minecraft/util/Identifier;)Lnet/minecraft/resource/Resource;"), cancellable = true)
    public void loadModelFromJson(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> cir) {
        //First, we check if the current item model that is being registered is from our mod. If it isn't, we continue.
        if (!"achievementbooks".equals(id.getNamespace())) return;
        String bookName = id.getPath().replace("item/", "");
        AchievementBookItem book = (AchievementBookItem) Registry.ITEM.get(new Identifier(AchievementBooks.MODID, bookName));

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
