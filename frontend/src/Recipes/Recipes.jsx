import "./Recipes.css";
import { useEffect, useState } from "react";
import axios from "axios";
import InventoryCard from "../components/InventoryCard/InventoryCard";
import Input from "../components/Input/Input";
const Recipes = () => {
  const [filters, setFilters] = useState([]);

  const [input, setInput] = useState("");

  const [recipesList, setRecipesList] = useState([]);

  const handleChange = (val) => {
    setInput(val.target.value);
  };

  useEffect(() => {
    axios({
      method: "post",
      url: "http://localhost:8080/api/recipe/list",
      headers: {},
      data: {
        user: {
          username: localStorage.getItem("username"),
          password: localStorage.getItem("password"),
        },
        filterBy: filters.map((el) => el.id),
      },
    })
      .then((r) => setRecipesList(r.data.resultList))
      .catch((err) => console.log(err));
  }, [filters]);

  const [allItems, setAllItems] = useState([]);

  useEffect(() => {
    axios({
      method: "post",
      url: "http://localhost:8080/api/item/list",
      headers: {},
      data: {},
    })
      .then((r) => setAllItems(r.data.resultList))
      .catch((err) => console.log(err));
  }, []);

  const Filters = () => (
    <div>
      <h2>Filters</h2>
      <p>Click on an ingredient from items to remove it from the filters.</p>

      <div id="filter">
        {filters.map((el) => {
          const item = el;

          return (
            <InventoryCard
              id={item.id}
              name={item.name}
              displayName={item.displayName}
              selectCard={onClickFilter}
              // selected={item.id === selectedItem}
              // count={count}
              key={item.id + 2000}
            />
          );
        })}
      </div>
    </div>
  );

  const onClickItem = (id) => {
    if (
      filters.length < 9 &&
      filters.filter((el) => el.id === id).length === 0
    ) {
      const item = allItems.filter((el) => el.id === id)[0];

      let newFilters = [...filters];

      newFilters.push(item);

      setFilters(newFilters);
    }
  };

  const onClickFilter = (id) => {
    let newFilters = [...filters];
    newFilters = newFilters.filter((el) => el.id !== id);
    setFilters(newFilters);
  };

  const Items = () => (
    <div className="itemsWrapper">
      <h2>All items</h2>
      <p>Click on an ingredient from items to add it to the filters.</p>
      <Input value={input} onChange={handleChange} />
      <div id="inventory">
        {allItems
          .filter((el) => {
            return el.displayName.toLowerCase().includes(input.toLowerCase());
            // return true;
          })
          .map((el) => {
            const item = el;

            return (
              <InventoryCard
                id={item.id}
                name={item.name}
                displayName={item.displayName}
                selectCard={onClickItem}
                // selected={item.id === selectedItem}
                // count={count}
                key={item.id}
              />
            );
          })}
      </div>
    </div>
  );

  const Result = (resultItem) => {
    if (resultItem === null) {
      return (
        <div className={'result'}>
          <InventoryCard
            id={0}
            selectCard={() => {}}
            key={"craft-res-" + 0}
            isAir={true}
          />
        </div>
        );
    }
    return (
      <div className={'result'}>
        <InventoryCard
          id={resultItem.resultItem.id}
          name={resultItem.resultItem.name}
          displayName={resultItem.resultItem.displayName}
          selectCard={() => {}}
          count={resultItem.resultItem.count}
//          htmlId={"result-" + resultItem.id}
        />
  </div>
    );
  };
  
//  console.log("HUY")
  
  const CraftingTable = (data) => {
    const resultItem = allItems.filter(el => el.id === data.resultItemId)[0];
    
//    console.log(resultItem)
    
    return  <div className="recipe">
        <div className="grid">
          {[
            data.craftMatrix[0][0],
            data.craftMatrix[0][1],
            data.craftMatrix[0][2],
            data.craftMatrix[1][0],
            data.craftMatrix[1][1],
            data.craftMatrix[1][2],
            data.craftMatrix[2][0],
            data.craftMatrix[2][1],
            data.craftMatrix[2][2],
          ].map((el, i) => {
            if (el === 0) {
              return (
                <InventoryCard
                  id={i}
                  selectCard={() => {}}
                  key={"craft-" + i + el.id}
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
                selectCard={() => {}}
                key={"craftTable-" + i}
                htmlId={"craftTable-" + i}
              />
            );
          })}
        </div>
        
        <div className="arrow">&#10132;</div>
      <Result resultItem={{
        count: data.resultItemCnt,
        id: data.resultItemId,
        name: resultItem.name,
        displayName: resultItem.displayName
      }} />
    </div>
  };
  
//  console.log(recipesList);
  
  const RecipesList = () => (
    <div className={"recipesList"}>
      <h2>Recipes</h2>
      {recipesList.map((data) => CraftingTable(data))}
    </div>
  );

  return (
    <div>
      <h1>Recipes</h1>
      <div>{Filters()}</div>
      <br />
      <br />
      <div>{Items()}</div>
      <br />
      <br />
      <div>{RecipesList()}</div>
    </div>
  );
};

export default Recipes;
