import React from "react";
import CraftingPage from "./CraftingPage/CraftingPage";
import Button from "./components/Button/Button";
import ButtonGroup from "./components/ButtonGroup/ButtonGroup";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import Recipes from "./Recipes/Recipes";
import LoginPage from "./LoginPage/LoginPage"
import { useNavigate } from "react-router-dom";
import axios from "axios";
import TradeOffers from "./TradeOffers/TradeOffers";

const App = () => {
  

  const navigate = useNavigate();
  const selectPage = (value) => {
    if (value === "open_chest") {
      axios({
        method: "post",
        url: "http://localhost:8080/api/chest",
        headers: {},
        data: {
          username: localStorage.getItem("username"),
          password: localStorage.getItem("password"),
        },
      })
        .then((r) => {
          // navigate(`/${value}`);
          navigate(`/`);
        })
        .catch((err) => console.log(err));
    } else navigate(`/${value}`);
  };

  return (
    <div>
      <ButtonGroup
        onChange={selectPage}
        options={[
          {
            label: "Crafting Table",
            value: "",
          },
          {
            label: "Recipes",
            value: "recipes",
          },
          {
            label: "Trade Offers",
            value: "trade_offers",
          },
          {
            label: "Open Chest",
            value: "open_chest",
          },
        ]}
      />
      <Routes>
        <Route path="/login" element={<LoginPage/>}/>
        <Route exact path="/" element={<CraftingPage />} />
        <Route path="/recipes" element={<Recipes />} />
        <Route path="/trade_offers" element={<TradeOffers />} />
      </Routes>
    </div>
  );
};

export default App;
