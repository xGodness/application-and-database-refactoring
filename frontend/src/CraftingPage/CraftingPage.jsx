import "./CraftingPage.css";
import { useState, useEffect } from "react";
import InventoryCard from "../components/InventoryCard/InventoryCard";
import axios from "axios";

import { useNavigate } from "react-router-dom";

const CraftingPage = () => {
  
  
  const navigate = useNavigate();
  
  const [craftingTableArray, setCraftingTableArray] = useState([
    0, 0, 0, 0, 0, 0, 0, 0, 0,
  ]);

  const [resultItem, setResultItem] = useState(null);

  const [inventory, setInventory] = useState([]);
  const [allItems, setAllItems] = useState([]);

  useEffect(() => {
    axios({
      method: "post",
      url: "http://localhost:8080/api/recipe/check",
      headers: {},
      data: {
        user: {
          username: localStorage.getItem("username"),
          password: localStorage.getItem("password"),
        },
        craftMatrix: [
          [craftingTableArray[0], craftingTableArray[1], craftingTableArray[2]],
          [craftingTableArray[3], craftingTableArray[4], craftingTableArray[5]],
          [craftingTableArray[6], craftingTableArray[7], craftingTableArray[8]],
        ],
      },
    })
      .then((r) => setResultItem(r.data.resultList[0]))
      .catch((err) => setResultItem(null));
  }, [craftingTableArray]);

  useEffect(() => {
    
    axios({
      method: "post",
      url: "http://localhost:8080/api/user/login",
      headers: {},
      data: {
        username: localStorage.getItem("username"),
        password: localStorage.getItem("password"),
      },
    })
      .then((r) => {

      })
      .catch((err) => navigate('/login'));
    
    setCraftingTableArray([0, 0, 0, 0, 0, 0, 0, 0, 0]);
    setResultItem(null);
    axios({
      method: "post",
      url: "http://localhost:8080/api/item/inventory",
      headers: {},
      data: {
        username: localStorage.getItem("username"),
        password: localStorage.getItem("password"),
      },
    })
      .then((r) => setInventory(r.data.resultList))
      .catch((err) => console.log(err));

    axios({
      method: "post",
      url: "http://localhost:8080/api/item/list",
      headers: {},
      data: {},
    })
      .then((r) => setAllItems(r.data.resultList))
      .catch((err) => console.log(err));

    console.log("REFRESHED");
  }, []);
  const selectCraftTable = (num) => {
    const craft2inventory = (id) => {
      let arr = [...craftingTableArray];
      let tempInventory = [...inventory];

      tempInventory = tempInventory.map((el) => {
        if (el.item.id === id) {
          let newEl = { ...el };
          newEl.count += 1;
          return newEl;
        }

        return el;
      });

      setInventory(tempInventory);

      arr[num] = 0;
      setCraftingTableArray(arr);
    };
    const inventory2craft = (id) => {
      let arr = [...craftingTableArray];
      let tempInventory = [...inventory];

      let flag_can = false;

      tempInventory = tempInventory.map((el) => {
        if (el.item.id === id) {
          if (el.count > 0) {
            let newEl = { ...el };
            newEl.count -= 1;
            return newEl;
          } else flag_can = true;
        }

        return el;
      });

      if (!flag_can) {
        setInventory(tempInventory);

        arr[num] = selectedItem;
        setCraftingTableArray(arr);
      }
    };

    if (selectedItem != null) {
      if (craftingTableArray[num] === 0) {
        inventory2craft(selectedItem);
      } else {
        craft2inventory(craftingTableArray[num]);
      }
    } else {
      if (craftingTableArray[num] !== 0) {
        craft2inventory(craftingTableArray[num]);
      }
    }
  };

  const selectResult = () => {
    axios({
      method: "post",
      url: "http://localhost:8080/api/item/craft",
      headers: {},
      data: {
        user: {
          username: localStorage.getItem("username"),
          password: localStorage.getItem("password"),
        },
        craftMatrix: [
          [craftingTableArray[0], craftingTableArray[1], craftingTableArray[2]],
          [craftingTableArray[3], craftingTableArray[4], craftingTableArray[5]],
          [craftingTableArray[6], craftingTableArray[7], craftingTableArray[8]],
        ],
      },
    })
      .then((r) => {
        axios({
          method: "post",
          url: "http://localhost:8080/api/item/inventory",
          headers: {},
          data: {
            username: localStorage.getItem("username"),
            password: localStorage.getItem("password"),
          },
        })
          .then((r) => setInventory(r.data.resultList))
          .catch((err) => console.log(err));

        setCraftingTableArray([0, 0, 0, 0, 0, 0, 0, 0, 0]);
        setResultItem(null);
      })
      .catch((err) => console.log(err));
  };

  const Result = () => {
    if (resultItem === null) {
      return (
        <div className={"Result"}>
          <InventoryCard
            id={0}
            selectCard={selectResult}
            key={"craft-res-" + 0}
            isAir={true}
          />
        </div>
      );
    }
    return (
      <div className="Result">
        <InventoryCard
          id={resultItem.item.id}
          name={resultItem.item.name}
          displayName={resultItem.item.displayName}
          selectCard={selectResult}
          count={resultItem.count}
          htmlId={"result"}
        />
      </div>
    );
  };

  const CraftingTable = () => (
    <div>
      <h1>Crafting Table</h1>
      <div className={"recipe"}>
      <div id="grid">
        {craftingTableArray.map((el, i) => {
          if (el === 0 || allItems === []) {
            return (
              <InventoryCard
                id={i}
                selectCard={selectCraftTable}
                key={"craft-" + i}
                isAir={true}
              />
            );
          }

          const item = allItems.filter((item) => item.id === el)[0];

          return (
            <InventoryCard
              id={i}
              name={item.name}
              displayName={item.displayName}
              selectCard={selectCraftTable}
              key={"craftTable-" + i}
              htmlId={"craftTable-" + i}
            />
          );
        })}
      </div>

      <div className="arrow">&#10132;</div>
      <Result />
      </div>
    </div>
  );

  const [selectedItem, setSelectedItem] = useState(null);

  const selectInventoryItem = (itemId) => {
    if (selectedItem === itemId) {
      setSelectedItem(null);
    } else {
      setSelectedItem(itemId);
    }
  };

  const Inventory = () => (
    <div>
      <h1>Inventory</h1>
      <p>
        Click on an ingredient from your inventory, then click on a cell of your
        crafting table to place this ingredient.
      </p>
      <div id="inventory">
        {inventory.map((el) => {
          const { item, count } = el;

          return (
            <InventoryCard
              id={item.id}
              name={item.name}
              displayName={item.displayName}
              selectCard={selectInventoryItem}
              selected={item.id === selectedItem}
              count={count}
              key={item.id}
            />
          );
        })}
      </div>
    </div>
  );

  return (
    <div>
      <div id="screen">
        <CraftingTable />
        <Inventory />
      </div>
    </div>
  );
};

export default CraftingPage;
