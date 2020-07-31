package team.unnamed.example;

import me.fixeddev.ebcm.parametric.CommandClass;
import me.fixeddev.ebcm.parametric.annotation.ACommand;
import me.fixeddev.ebcm.parametric.annotation.Injected;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import team.unnamed.gui.item.DefaultItemClickable;
import team.unnamed.gui.menu.MenuBuilder;

@ACommand(names = "menu")
public class ExampleCommand implements CommandClass {

    @ACommand(names = "")
    public boolean menuCommand(@Injected(true) CommandSender sender) {
        Player player = (Player) sender;

        MenuBuilder menuBuilder = MenuBuilder.newBuilder("Simple test")
                .addItem(
                        14,
                        new DefaultItemClickable(
                                new ItemStack(Material.ENDER_PEARL),
                                click -> {
                                    player.sendMessage("Just testing...");

                                    return true;
                                }
                        )
                );

        player.openInventory(menuBuilder.build());

        return true;
    }

}