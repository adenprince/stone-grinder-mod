package net.nightfallclosure.stonegrinder.rei.plugin.client.categories;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.BurningFire;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.plugin.client.categories.cooking.DefaultCookingCategory;
import me.shedaniel.rei.plugin.common.displays.cooking.DefaultCookingDisplay;
import net.nightfallclosure.stonegrinder.rei.plugin.client.gui.widgets.DrillWidget;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

// TODO: Move class to common folder
public class DefaultGrindingCategory extends DefaultCookingCategory {
    public DefaultGrindingCategory(CategoryIdentifier<? extends DefaultCookingDisplay> identifier, EntryStack<?> logo,
                                   String categoryName) {
        super(identifier, logo, categoryName);
    }

    @Override
    public List<Widget> setupDisplay(DefaultCookingDisplay display, Rectangle bounds) {
        List<Widget> defaultCookingCategoryWidgets = super.setupDisplay(display, bounds);

        Stream<Slot> defaultCookingCategorySlots = defaultCookingCategoryWidgets
                .stream()
                .filter(x -> x instanceof Slot)
                .map(x -> (Slot)x);

        // Move input slot down
        Optional<Slot> cookingInputSlot = defaultCookingCategorySlots
                .min(Comparator.comparingInt(x -> x.getBounds().getX()));
        cookingInputSlot.ifPresent(slot -> slot.getBounds().y += 13);

        // Move burning fire up and left
        Optional<BurningFire> defaultCookingCategoryBurningFire = defaultCookingCategoryWidgets
                .stream()
                .filter(x -> x instanceof BurningFire)
                .map(x -> (BurningFire)x)
                .findFirst();
        defaultCookingCategoryBurningFire.ifPresent(burningFire -> {
            burningFire.getBounds().x -= 18;
            burningFire.getBounds().y -= 6;
        });

        cookingInputSlot.ifPresent(slot -> {
            Rectangle drillBounds = new Rectangle(
                    slot.getBounds().getX() + 2, slot.getBounds().getY() - 16, 14, 14);
            Widget drillWidget = new DrillWidget(drillBounds);
            defaultCookingCategoryWidgets.add(drillWidget);
        });

        return defaultCookingCategoryWidgets;
    }
}
