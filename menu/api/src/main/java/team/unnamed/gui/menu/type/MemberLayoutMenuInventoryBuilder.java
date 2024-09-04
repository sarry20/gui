package team.unnamed.gui.menu.type;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import team.unnamed.gui.menu.item.ItemClickable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static team.unnamed.validate.Validate.isNotNull;
import static team.unnamed.validate.Validate.isState;

public class MemberLayoutMenuInventoryBuilder
        extends MenuInventoryBuilderLayout<MemberLayoutMenuInventoryBuilder> {

    protected final Map<Character, ItemClickable> layoutItems;
    protected final List<String> layoutLines;
    protected final Character character;
    protected final List<String> members;
    protected final NamespacedKey key;

    protected MemberLayoutMenuInventoryBuilder(Component title, int rows, List<String> members , Character character, NamespacedKey key) {
        super(title, rows);
        this.layoutLines = new ArrayList<>(rows);
        this.key = key;
        this.layoutItems = new HashMap<>();
        this.character = character;
        this.members = members;
    }

    public MemberLayoutMenuInventoryBuilder layoutItem(char identifier, ItemClickable item) {
        this.layoutItems.put(identifier, isNotNull(item));
        return back();
    }

    public MemberLayoutMenuInventoryBuilder layoutLines(Iterable<String> lines) {
        for (String line : lines) {
            line = line.trim();
            isState(line.length() == 9,
                    "Cannot add layout line '" + line + "' because length is minor than 9");
            this.layoutLines.add(line.trim());
        }

        return back();
    }

    public MemberLayoutMenuInventoryBuilder layoutLines(String... lines) {
        return layoutLines(Arrays.asList(lines));
    }

    @Override
    public Inventory build() {
        int slotIndex = 0;
        int memberCount = 0;

        for (String layoutLine : this.layoutLines) {
            for (char c : layoutLine.toCharArray()) {
                ItemClickable itemClickable = this.layoutItems.get(c);

                if (itemClickable == null) {
                    slotIndex++;
                    continue;
                }

                if (c == character) {
                    if (members.size() <= memberCount) {
                        item(ItemClickable.onlyItem(new ItemStack(Material.AIR)).clone(slotIndex));
                        slotIndex++;
                        continue;
                    }
                    String playerName = members.get(memberCount);
                    ItemStack item = itemClickable.getItemStack().clone();
                    SkullMeta meta = (SkullMeta) item.getItemMeta();
                    PersistentDataContainer container = meta.getPersistentDataContainer();
                    container.set(key, PersistentDataType.STRING, playerName);
                    meta.setOwningPlayer(Bukkit.getOfflinePlayerIfCached(playerName));
                    meta.displayName(meta.displayName().replaceText(
                            TextReplacementConfig.builder()
                                    .match("%player%")
                                    .replacement(playerName)
                                    .build()
                    ));
                    List<Component> newLore = new ArrayList<>();
                    for (Component line : meta.lore()) {
                        newLore.add(line.replaceText(
                                TextReplacementConfig.builder()
                                        .match("%player%")
                                        .replacement(playerName)
                                        .build()
                        ));
                    }
                    meta.lore(newLore);
                    item.setItemMeta(meta);

                    item(ItemClickable.of(slotIndex, item, itemClickable.getAction()));

                    slotIndex++;
                    memberCount++;
                    continue;
                }
                item(itemClickable.clone(slotIndex));
                slotIndex++;
            }
        }

        return super.build();
    }

    @Override
    protected MemberLayoutMenuInventoryBuilder back() {
        return this;
    }

}
