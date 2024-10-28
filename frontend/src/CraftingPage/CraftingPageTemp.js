import "./CraftingPage.css";
import {
  ARROW,
  BOW,
  BUCKET,
  CHEST,
  COAL,
  COBBLESTONE,
  CRAFTING_TABLE,
  DIAMOND,
  DIAMOND_AXE,
  DIAMOND_HOE,
  DIAMOND_PICKAXE,
  DIAMOND_SHOVEL,
  DIAMOND_SWORD,
  FEATHER,
  FISHING_ROD,
  FLINT,
  FLINT_AND_STEEL,
  FURNACE,
  GOLD,
  GOLDEN_AXE,
  GOLDEN_HOE,
  GOLDEN_PICKAXE,
  GOLDEN_SHOVEL,
  GOLDEN_SWORD,
  IRON_AXE,
  IRON_HOE,
  IRON_INGOT,
  IRON_PICKAXE,
  IRON_SHOVEL,
  IRON_SWORD,
  REDSTONE,
  REDSTONE_BLOCK,
  REDSTONETORCH,
  STICK,
  STONE_AXE,
  STONE_HOE,
  STONE_PICKAXE,
  STONE_SHOVEL,
  STONE_SWORD,
  STRING,
  TORCH,
  WOOD,
  WOOD_PLANK,
  WOODEN_AXE,
  WOODEN_HOE,
  WOODEN_PICKAXE,
  WOODEN_SHOVEL,
  WOODEN_SWORD,
} from "./stubs/item-stub";

const CraftingPageTemp = () => {
  var craftTable = [0, 0, 0, 0, 0, 0, 0, 0, 0];
  var inventory = [
    COBBLESTONE,
    WOOD,
    IRON_INGOT,
    GOLD,
    DIAMOND,
    REDSTONE,
    STRING,
    FEATHER,
    FLINT,
    COAL,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
  ];
  var selectedCell;
  var selectedIngredient = 0;
  var newItem = 0;

  var recipes = [];
  recipes.push(["Wood Planks", WOOD_PLANK, [0, 0, 0, 0, WOOD, 0, 0, 0, 0]]);
  recipes.push(["Stick", STICK, [0, 0, 0, 0, WOOD_PLANK, 0, 0, WOOD_PLANK, 0]]);
  recipes.push([
    "Bow",
    BOW,
    [STRING, STICK, 0, STRING, 0, STICK, STRING, STICK, 0],
  ]);
  recipes.push(["Arrow", ARROW, [0, FLINT, 0, 0, STICK, 0, 0, FEATHER, 0]]);
  recipes.push(["Torch", TORCH, [0, 0, 0, 0, COAL, 0, 0, STICK, 0]]);
  recipes.push([
    "Redstone Torch",
    REDSTONETORCH,
    [0, 0, 0, 0, REDSTONE, 0, 0, STICK, 0],
  ]);
  recipes.push([
    "Redstone Block",
    REDSTONE_BLOCK,
    [
      REDSTONE,
      REDSTONE,
      REDSTONE,
      REDSTONE,
      REDSTONE,
      REDSTONE,
      REDSTONE,
      REDSTONE,
      REDSTONE,
    ],
  ]);
  recipes.push([
    "Chest",
    CHEST,
    [
      WOOD_PLANK,
      WOOD_PLANK,
      WOOD_PLANK,
      WOOD_PLANK,
      0,
      WOOD_PLANK,
      WOOD_PLANK,
      WOOD_PLANK,
      WOOD_PLANK,
    ],
  ]);
  recipes.push([
    "Crafting Table",
    CRAFTING_TABLE,
    [0, 0, 0, WOOD_PLANK, WOOD_PLANK, 0, WOOD_PLANK, WOOD_PLANK, 0],
  ]);
  recipes.push([
    "Furnace",
    FURNACE,
    [
      COBBLESTONE,
      COBBLESTONE,
      COBBLESTONE,
      COBBLESTONE,
      0,
      COBBLESTONE,
      COBBLESTONE,
      COBBLESTONE,
      COBBLESTONE,
    ],
  ]);
  recipes.push([
    "Bucket",
    BUCKET,
    [0, 0, 0, IRON_INGOT, 0, IRON_INGOT, 0, IRON_INGOT, 0],
  ]);
  recipes.push([
    "Flint and Steel",
    FLINT_AND_STEEL,
    [0, 0, 0, IRON_INGOT, 0, 0, 0, FLINT, 0],
  ]);
  recipes.push([
    "Wooden Axe",
    WOODEN_AXE,
    [WOOD_PLANK, WOOD_PLANK, 0, WOOD_PLANK, STICK, 0, 0, STICK, 0],
  ]);
  recipes.push([
    "Stone Axe",
    STONE_AXE,
    [COBBLESTONE, COBBLESTONE, 0, COBBLESTONE, STICK, 0, 0, STICK, 0],
  ]);
  recipes.push([
    "Iron Axe",
    IRON_AXE,
    [IRON_INGOT, IRON_INGOT, 0, IRON_INGOT, STICK, 0, 0, STICK, 0],
  ]);
  recipes.push([
    "Diamond Axe",
    DIAMOND_AXE,
    [DIAMOND, DIAMOND, 0, DIAMOND, STICK, 0, 0, STICK, 0],
  ]);
  recipes.push([
    "Golden Axe",
    GOLDEN_AXE,
    [GOLD, GOLD, 0, GOLD, STICK, 0, 0, STICK, 0],
  ]);
  recipes.push([
    "Wooden Hoe",
    WOODEN_HOE,
    [WOOD_PLANK, WOOD_PLANK, 0, 0, STICK, 0, 0, STICK, 0],
  ]);
  recipes.push([
    "Stone Hoe",
    STONE_HOE,
    [COBBLESTONE, COBBLESTONE, 0, 0, STICK, 0, 0, STICK, 0],
  ]);
  recipes.push([
    "Iron Hoe",
    IRON_HOE,
    [IRON_INGOT, IRON_INGOT, 0, 0, STICK, 0, 0, STICK, 0],
  ]);
  recipes.push([
    "Diamond Hoe",
    DIAMOND_HOE,
    [DIAMOND, DIAMOND, 0, 0, STICK, 0, 0, STICK, 0],
  ]);
  recipes.push([
    "Golden Hoe",
    GOLDEN_HOE,
    [GOLD, GOLD, 0, 0, STICK, 0, 0, STICK, 0],
  ]);
  recipes.push([
    "Wooden Pickaxe",
    WOODEN_PICKAXE,
    [WOOD_PLANK, WOOD_PLANK, WOOD_PLANK, 0, STICK, 0, 0, STICK, 0],
  ]);
  recipes.push([
    "Stone Pickaxe",
    STONE_PICKAXE,
    [COBBLESTONE, COBBLESTONE, COBBLESTONE, 0, STICK, 0, 0, STICK, 0],
  ]);
  recipes.push([
    "Iron Pickaxe",
    IRON_PICKAXE,
    [IRON_INGOT, IRON_INGOT, IRON_INGOT, 0, STICK, 0, 0, STICK, 0],
  ]);
  recipes.push([
    "Diamond Pickaxe",
    DIAMOND_PICKAXE,
    [DIAMOND, DIAMOND, DIAMOND, 0, STICK, 0, 0, STICK, 0],
  ]);
  recipes.push([
    "Golden Pickaxe",
    GOLDEN_PICKAXE,
    [GOLD, GOLD, GOLD, 0, STICK, 0, 0, STICK, 0],
  ]);
  recipes.push([
    "Wooden Shovel",
    WOODEN_SHOVEL,
    [0, WOOD_PLANK, 0, 0, STICK, 0, 0, STICK, 0],
  ]);
  recipes.push([
    "Stone Shovel",
    STONE_SHOVEL,
    [0, COBBLESTONE, 0, 0, STICK, 0, 0, STICK, 0],
  ]);
  recipes.push([
    "Iron Shovel",
    IRON_SHOVEL,
    [0, IRON_INGOT, 0, 0, STICK, 0, 0, STICK, 0],
  ]);
  recipes.push([
    "Diamond Shovel",
    DIAMOND_SHOVEL,
    [0, DIAMOND, 0, 0, STICK, 0, 0, STICK, 0],
  ]);
  recipes.push([
    "Golden Shovel",
    GOLDEN_SHOVEL,
    [0, GOLD, 0, 0, STICK, 0, 0, STICK, 0],
  ]);
  recipes.push([
    "Wooden Sword",
    WOODEN_SWORD,
    [0, WOOD_PLANK, 0, 0, WOOD_PLANK, 0, 0, STICK, 0],
  ]);
  recipes.push([
    "Stone Sword",
    STONE_SWORD,
    [0, COBBLESTONE, 0, 0, COBBLESTONE, 0, 0, STICK, 0],
  ]);
  recipes.push([
    "Iron Sword",
    IRON_SWORD,
    [0, IRON_INGOT, 0, 0, IRON_INGOT, 0, 0, STICK, 0],
  ]);
  recipes.push([
    "Diamond Sword",
    DIAMOND_SWORD,
    [0, DIAMOND, 0, 0, DIAMOND, 0, 0, STICK, 0],
  ]);
  recipes.push([
    "Golden Sword",
    GOLDEN_SWORD,
    [0, GOLD, 0, 0, GOLD, 0, 0, STICK, 0],
  ]);
  recipes.push([
    "Fishing Rod",
    FISHING_ROD,
    [0, 0, STICK, 0, STICK, STRING, STICK, 0, STRING],
  ]);

  //Add newly crafted items_pictures to the inventory (if it's not already there)
  function addItemToInventory() {
    var inventoryIsFull = true;
    if (newItem != 0) {
      //First check if this items_pictures is not already in the inventory
      if (inventory.indexOf(newItem) == -1) {
        //Then find an empty location in the inventory
        for (var i = 0; i < inventory.length; i++) {
          if (inventory[i] == 0) {
            //Empty location spotted. Add items_pictures to the inventory
            inventoryIsFull = false;
            inventory[i] = newItem;
            document.getElementById("inventory-" + i).innerHTML =
              "<IMG src='http://www.101computing.net/mc/" +
              +newItem +
              "-0.png'>";
            break;
          }
        }
        if (inventoryIsFull) alert("Inventory is full!");
      } else {
        alert("This items_pictures is already in your inventory!");
      }
    }
  }

  //A function to compare a recipe with the content of the craft table
  function checkRecipe(recipe) {
    var match = true;
    for (var i = 0; i < 9; i++) {
      if (recipe[i] != craftTable[i]) {
        match = false;
        break;
      }
    }
    return match;
  }

  //A function to compare the craft table with all recipes to see if an items_pictures can be crafted
  function craft() {
    //Check each recipe one at a time
    document.getElementById("result").innerHTML = "";
    newItem = "";
    for (var i = 0; i < recipes.length; i++) {
      if (checkRecipe(recipes[i][2])) {
        newItem = recipes[i][1];
        //Craft the new items_pictures!
        document.getElementById("result").innerHTML =
          "<IMG src='http://www.101computing.net/mc/" +
          +recipes[i][1] +
          "-0.png'><br/>" +
          recipes[i][0] +
          "<BR/>Click on this items_pictures to add it to your inventory.";
        break;
      }
    }
  }

  //Highlight inventory items_pictures when user click on it
  function selectInventoryItem(cell_ID) {
    if (selectedCell) {
      selectedCell.style.backgroundColor = "#8b8b8b";
    }
    selectedCell = document.getElementById("inventory-" + cell_ID);
    selectedCell.style.backgroundColor = "#88FF88";
    selectedIngredient = inventory[cell_ID];
  }

  //Replace ingredient on craft table when the user click on one of the 9 cells of the craft table
  function selectCraftTable(cell_ID) {
    var craftTableCell = document.getElementById("craftTable-" + cell_ID);
    if (craftTableCell.innerHTML == "") {
      if (selectedCell) {
        craftTableCell.innerHTML = selectedCell.innerHTML;
        craftTable[cell_ID] = selectedIngredient;
      }
    } else {
      craftTableCell.innerHTML = "";
      craftTable[cell_ID] = 0;
    }
    craft();
  }

  return (
    <div>
      <div id="screen">
        <h1>Crafting Table</h1>
        <div id="grid">
          <div
            className="gridCell"
            onClick={() => selectCraftTable(0)}
            id="craftTable-0"
          ></div>
          <div
            className="gridCell"
            onClick={() => selectCraftTable(1)}
            id="craftTable-1"
          ></div>
          <div
            className="gridCell"
            onClick={() => selectCraftTable(2)}
            id="craftTable-2"
          ></div>
          <div
            className="gridCell"
            onClick={() => selectCraftTable(3)}
            id="craftTable-3"
          ></div>
          <div
            className="gridCell"
            onClick={() => selectCraftTable(4)}
            id="craftTable-4"
          ></div>
          <div
            className="gridCell"
            onClick={() => selectCraftTable(5)}
            id="craftTable-5"
          ></div>
          <div
            className="gridCell"
            onClick={() => selectCraftTable(6)}
            id="craftTable-6"
          ></div>
          <div
            className="gridCell"
            onClick={() => selectCraftTable(7)}
            id="craftTable-7"
          ></div>
          <div
            className="gridCell"
            onClick={() => selectCraftTable(8)}
            id="craftTable-8"
          ></div>
        </div>
        <div className="arrow">&#10132;</div>
        <div id="result" onClick={addItemToInventory}></div>

        <h1>Inventory</h1>
        <p>
          Click on an ingredient from your inventory, then click on a cell of
          your crafting table to place this ingredient.
        </p>
        <div id="inventory">
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(0)}
            id="inventory-0"
          >
            <img
              src="http://www.101computing.net/mc/4-0.png"
              alt="Cobblestone"
            ></img>
          </div>
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(1)}
            id="inventory-1"
          >
            <img src="http://www.101computing.net/mc/17-0.png" alt="Wood"></img>
          </div>
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(2)}
            id="inventory-2"
          >
            <img
              src="http://www.101computing.net/mc/265-0.png"
              alt="Iron ingot"
            ></img>
          </div>
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(3)}
            id="inventory-3"
          >
            <img
              src="http://www.101computing.net/mc/266-0.png"
              alt="Gold"
            ></img>
          </div>
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(4)}
            id="inventory-4"
          >
            <img
              src="http://www.101computing.net/mc/264-0.png"
              alt="Diamond"
            ></img>
          </div>
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(5)}
            id="inventory-5"
          >
            <img
              src="http://www.101computing.net/mc/331-0.png"
              alt="Red Stone"
            ></img>
          </div>
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(6)}
            id="inventory-6"
          >
            <img
              src="http://www.101computing.net/mc/287-0.png"
              alt="String"
            ></img>
          </div>
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(7)}
            id="inventory-7"
          >
            <img
              src="http://www.101computing.net/mc/288-0.png"
              alt="Feather"
            ></img>
          </div>
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(8)}
            id="inventory-8"
          >
            <img
              src="http://www.101computing.net/mc/318-0.png"
              alt="Flint"
            ></img>
          </div>
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(9)}
            id="inventory-9"
          >
            <img
              src="http://www.101computing.net/mc/263-0.png"
              alt="Coal"
            ></img>
          </div>
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(10)}
            id="inventory-10"
          ></div>
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(11)}
            id="inventory-11"
          ></div>
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(12)}
            id="inventory-12"
          ></div>
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(13)}
            id="inventory-13"
          ></div>
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(14)}
            id="inventory-14"
          ></div>
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(15)}
            id="inventory-15"
          ></div>
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(16)}
            id="inventory-16"
          ></div>
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(17)}
            id="inventory-17"
          ></div>
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(18)}
            id="inventory-18"
          ></div>
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(19)}
            id="inventory-19"
          ></div>
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(20)}
            id="inventory-20"
          ></div>
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(21)}
            id="inventory-21"
          ></div>
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(22)}
            id="inventory-22"
          ></div>
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(23)}
            id="inventory-23"
          ></div>
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(24)}
            id="inventory-24"
          ></div>
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(25)}
            id="inventory-25"
          ></div>
          <div
            className="gridCell"
            onClick={() => selectInventoryItem(26)}
            id="inventory-26"
          ></div>
        </div>
        <h1>
          <br />
          Recipes
        </h1>
        <div id="recipes">
          <p>Wooden planks:</p>
          <img src="http://www.101computing.net/mc/recipe-wood-plank.png"></img>
          <p>Sticks:</p>
          <img src="http://www.101computing.net/mc/recipe-stick.png"></img>
          <p>Chest:</p>
          <img src="http://www.101computing.net/mc/recipe-chest.png"></img>
          <p>Crafting Table:</p>
          <img src="http://www.101computing.net/mc/recipe-crafting-table.png"></img>
          <p>Furnace:</p>
          <img src="http://www.101computing.net/mc/recipe-furnace.png"></img>
          <p>Flint and Steel:</p>
          <img src="http://www.101computing.net/mc/recipe-flint-and-steel.png"></img>
          <p>Redstone Block:</p>
          <img src="http://www.101computing.net/mc/recipe-redstone-block.png"></img>
          <p>Swords:</p>
          <img src="http://www.101computing.net/mc/recipe-swords.gif"></img>
          <p>Fishing Rod:</p>
          <img src="http://www.101computing.net/mc/recipe-fishing-rod.png"></img>
          <p>Bow:</p>
          <img src="http://www.101computing.net/mc/recipe-bow.png"></img>
          <p>Arrows:</p>
          <img src="http://www.101computing.net/mc/recipe-arrow.png"></img>
          <p>Bucket:</p>
          <img src="http://www.101computing.net/mc/recipe-bucket.png"></img>
          <p>Hoes:</p>
          <img src="http://www.101computing.net/mc/recipe-hoes.gif"></img>
          <p>Axes:</p>
          <img src="http://www.101computing.net/mc/recipe-axes.gif"></img>
          <p>pickaxes:</p>
          <img src="http://www.101computing.net/mc/recipe-pickaxes.gif"></img>
          <p>Shovels:</p>
          <img src="http://www.101computing.net/mc/recipe-shovels.gif"></img>
        </div>
      </div>
    </div>
  );
};

export default CraftingPageTemp;
