import React, {useEffect, useState} from "react";
import CraftingPage from "./CraftingPage/CraftingPage";
import Button from "./components/Button/Button";
import ButtonGroup from "./components/ButtonGroup/ButtonGroup";
import { Routes, Route} from "react-router-dom";
import Recipes from "./Recipes/Recipes";
import LoginPage from "./LoginPage/LoginPage"
import { useNavigate } from "react-router-dom";
import axios from "axios";
import TradeOffers from "./TradeOffers/TradeOffers";
import AdminPage from "./AdminPage/AdminPage";

const App = () => {

    const [username, setUsername] = useState(localStorage.getItem("username"));
    const [password, setPassword] = useState(localStorage.getItem("password"));
    const [loggedIn, setLoggedIn] = useState(false);
    const [admin, setAdmin] = useState(false);

    const navigate = useNavigate();


    useEffect(() => {
        axios({
            method: "post",
            url: "http://localhost:8080/api/user/login",
            headers: {},
            data: {
                username: username,
                password: password,
            },
        })
            .then(() => {
                setLoggedIn(true)
                console.log("Logged in")

            })
            .catch(() => {
                setLoggedIn(false);
                navigate('/login');
            });

        axios({
            method: "post",
            url: "http://localhost:8080/api/user/amiadmin",
            headers: {},
            data: {
                username: username,
                password: password,
            },
        })
            .then(() => {
                setAdmin(true)
                console.log("Admin")

            })
            .catch(() => {
                setAdmin(false);
            });
    }, [localStorage.getItem("username"), localStorage.getItem("password")]);

    const logOut = () => {
        setLoggedIn(false);
        setAdmin(false);

        setUsername(null);
        setPassword(null);
        localStorage.setItem("username", null);
        localStorage.setItem("password", null);
        navigate('/login');
    }

  const selectPage = (value) => {
    if (!loggedIn) navigate('/login');

    if (value === "open_chest") {
      axios({
        method: "post",
        url: "http://localhost:8080/api/chest",
        headers: {},
        data: {
          username: username,
          password: password
        },
      })
        .then(() => {
          navigate(0);
        })
        .catch((err) => console.log(err));
    } else navigate(`/${value}`);
  };

    const options = [
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
    ]

  return (
    <div>
        <div style={{ display: "flex", justifyContent:'space-between' }}>
          <ButtonGroup
            onChange={selectPage}
            options={admin ? [...options, {label: "Admin Page", value: "admin_page"}] : options}
          />

            {loggedIn ?
                <div style={{ display: "flex", justifyContent:'space-around', alignItems: 'center'}}>
                    <div style={{padding:'10px'}}>{username}</div>
                    <Button
                        onClick={logOut}
                    >
                        Logout
                    </Button>
                </div>
                : null}

        </div>
      <Routes>
        <Route path="/login" element={<LoginPage/>}/>
        <Route exact path="/" element={<CraftingPage />} />
        <Route path="/recipes" element={<Recipes />} />
        <Route path="/trade_offers" element={<TradeOffers />} />
        <Route path={"/admin_page"} element={<AdminPage />} />
      </Routes>
    </div>
  );
};

export default App;
