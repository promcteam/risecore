package me.travja.darkrise.core.legacy.util.item;

import me.travja.darkrise.core.legacy.util.ItemUtils;
import me.travja.darkrise.core.legacy.util.DeserializationWorker;
import me.travja.darkrise.core.legacy.util.SerializationBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bukkit.FireworkEffect;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class FireworkEffectBuilder extends DataBuilder
{
    private FireworkEffect effect;

    public FireworkEffectBuilder()
    {
    }

    @SuppressWarnings("unchecked")
    public FireworkEffectBuilder(final Map<String, Object> map)
    {
        final DeserializationWorker w = DeserializationWorker.start(map);
        this.effect = ItemUtils.simpleDeserializeEffect(w.getTypedObject("effect"));
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).append("effect", this.effect).toString();
    }

    public FireworkEffect getEffect()
    {
        return this.effect;
    }

    public FireworkEffectBuilder effect(final FireworkEffect effect)
    {
        this.effect = effect;
        return this;
    }

    public FireworkEffectBuilder effect(final FireworkEffect.Builder effect)
    {
        this.effect = effect.build();
        return this;
    }

    @Override
    public void apply(final ItemMeta itemMeta)
    {
        if (! (itemMeta instanceof FireworkEffectMeta))
        {
            return;
        }
        final FireworkEffectMeta meta = (FireworkEffectMeta) itemMeta;
        meta.setEffect(this.effect);
    }

    @Override
    public FireworkEffectBuilder use(final ItemMeta itemMeta)
    {
        if (! (itemMeta instanceof FireworkEffectMeta))
        {
            return null;
        }
        final FireworkEffectMeta meta = (FireworkEffectMeta) itemMeta;
        this.effect = meta.getEffect();
        return this;
    }

    @Override
    public String getType()
    {
        return "firework_effect";
    }

    @Override
    public Map<String, Object> serialize()
    {
        final SerializationBuilder b = SerializationBuilder.start(2).append(super.serialize());
        b.append("effect", ItemUtils.simpleSerializeEffect(this.effect));
        return b.build();
    }

    public static FireworkEffectBuilder start()
    {
        return new FireworkEffectBuilder();
    }
}
