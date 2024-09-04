package team.unnamed.gui.menu.type;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import team.unnamed.gui.menu.item.ItemClickable;

import java.util.*;

import static team.unnamed.validate.Validate.isNotNull;
import static team.unnamed.validate.Validate.isState;

public class CustomLayoutMenuInventoryBuilder
        extends MenuInventoryBuilderLayout<CustomLayoutMenuInventoryBuilder> {

    protected final Map<Character, ItemClickable> layoutItems;
    protected final List<String> layoutLines;
    protected int usableSlots;
    protected Map<String, Integer> map;
    protected final Character inventoryChar;
    protected final Character fuelChar;
    protected final Character upgradeChar;
    protected final List<ItemStack> fuels = new ArrayList<>();
    protected final List<ItemStack> upgrades = new ArrayList<>();

    protected CustomLayoutMenuInventoryBuilder(Component title, int rows, Map<String, Integer> map, int usableSlots, Character character, Character fuelChar, Character upgradeChar, List<ItemStack> fuels, List<ItemStack> upgrades) {
        super(title, rows);
        this.layoutLines = new ArrayList<>(rows);
        this.layoutItems = new HashMap<>();
        this.map = map;
        this.usableSlots = usableSlots;
        this.inventoryChar = character;
        this.fuelChar = fuelChar;
        this.upgradeChar = upgradeChar;
        this.fuels.addAll(fuels);
        this.upgrades.addAll(upgrades);
    }

    public CustomLayoutMenuInventoryBuilder layoutItem(char identifier, ItemClickable item) {
        this.layoutItems.put(identifier, isNotNull(item));
        return back();
    }

    public CustomLayoutMenuInventoryBuilder layoutLines(Iterable<String> lines) {
        for (String line : lines) {
            line = line.trim();
            isState(line.length() == 9,
                    "Cannot add layout line '" + line + "' because length is minor than 9");
            this.layoutLines.add(line.trim());
        }

        return back();
    }

    public CustomLayoutMenuInventoryBuilder layoutLines(String... lines) {
        return layoutLines(Arrays.asList(lines));
    }

    @Override
    public Inventory build() {
        int slotIndex = 0;
        int fuelIndex = 0;
        int upgradeIndex = 0;

        for (String layoutLine : this.layoutLines) {
            for (char c : layoutLine.toCharArray()) {
                ItemClickable itemClickable = this.layoutItems.get(c);

                if (itemClickable == null) {
                    slotIndex++;
                    continue;
                }

                if (c == inventoryChar) {
                    if (usableSlots == 0) {
                        item(itemClickable.clone(slotIndex));
                        slotIndex++;
                        continue;
                    }
                    ItemStack item = new ItemStack(Material.AIR);

                    for (var entry : map.entrySet()) {
                        if (entry.getValue() > 0) {
                            item = new ItemStack(Material.matchMaterial(entry.getKey()));
                            if (entry.getValue() > 64) {
                                item.setAmount(64);
                                entry.setValue(entry.getValue() - 64);
                                break;
                            } else {
                                item.setAmount(entry.getValue());
                                entry.setValue(0);
                                break;
                            }
                        }
                    }

                    item(ItemClickable.of(slotIndex, item, itemClickable.getAction()));
                    usableSlots--;
                    slotIndex++;
                    continue;
                } else if (c == fuelChar) {
                    if (fuels.size() <= fuelIndex) {
                        item(itemClickable.clone(slotIndex));
                        slotIndex++;
                        continue;
                    }
                    item(ItemClickable.onlyItem(fuels.get(fuelIndex), itemClickable.getAction()).clone(slotIndex));
                    fuelIndex++;
                    slotIndex++;
                    continue;
                } else if (c == upgradeChar) {
                    if (upgrades.size() <= upgradeIndex) {
                        item(itemClickable.clone(slotIndex));
                        slotIndex++;
                        continue;
                    }
                    item(ItemClickable.onlyItem(upgrades.get(upgradeIndex), itemClickable.getAction()).clone(slotIndex));
                    upgradeIndex++;
                    slotIndex++;
                    continue;
                }
                item(itemClickable.clone(slotIndex));
                slotIndex++;
            }
        }

        return super.build();
    }

    @Override
    protected CustomLayoutMenuInventoryBuilder back() {
        return this;
    }

}
