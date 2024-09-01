package team.unnamed.gui.menu.type;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.gui.menu.item.ItemClickable;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public interface MenuInventory {

    static MenuInventoryBuilder newBuilder(Component title) {
        return new DefaultMenuInventoryBuilder(title);
    }

    static MenuInventoryBuilder newBuilder(Component title, int rows) {
        return new DefaultMenuInventoryBuilder(title, rows);
    }

    static StringLayoutMenuInventoryBuilder newStringLayoutBuilder(Component title) {
        return new StringLayoutMenuInventoryBuilder(title);
    }

    static StringLayoutMenuInventoryBuilder newStringLayoutBuilder(Component title, int rows) {
        return new StringLayoutMenuInventoryBuilder(title, rows);
    }
    static CustomLayoutMenuInventoryBuilder newCustomLayoutBuilder(Component title, int rows, Map<String,Integer> map, int usableSlots, char character) {
        return new CustomLayoutMenuInventoryBuilder(title, rows,map, usableSlots, character);
    }

    static <E> PaginatedMenuInventoryBuilder<E> newPaginatedBuilder(Class<E> entityType, Component title) {
        return new PaginatedMenuInventoryBuilder<>(title);
    }

    static <E> PaginatedMenuInventoryBuilder<E> newPaginatedBuilder(Class<E> entityType, Component title, int rows) {
        return new PaginatedMenuInventoryBuilder<>(title, rows);
    }

    @NotNull
    Component getTitle();

    int getSlots();

    @NotNull
    List<ItemClickable> getItems();

    void clearItems();

    void setItem(ItemClickable item);

    void removeItem(int slot);

    @Nullable Predicate<Inventory> getOpenAction();

    @Nullable Predicate<Inventory> getCloseAction();

    boolean canIntroduceItems();

    boolean canDragItems();

    default @Nullable ItemClickable getItem(int slot) {
        return getItems().get(slot);
    }

}
