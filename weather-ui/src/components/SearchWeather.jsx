import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./SearchWeather.css";

const SearchWeather = () => {
  const [city, setCity] = useState("");
  const [errorPopup, setErrorPopup] = useState("");
  const navigate = useNavigate();

  const handleSearch = async () => {
    if (!city.trim()) {
      setErrorPopup("Please enter a valid city name");
      return;
    }

    try {
      // const API_KEY = "API KEY";

      const response = await fetch(
        `https://api.openweathermap.org/data/2.5/weather?q=${city.trim()}&appid=${API_KEY}&units=metric`
      );

      const data = await response.json();

      if (data.cod === 401) {
        setErrorPopup("Invalid API key. Please update your API key.");
        return;
      }

      if (data.cod === "404") {
        setErrorPopup("City not found. Please try again!");
        return;
      }

      navigate(`/weather/${city.trim()}`);

    } catch (error) {
      setErrorPopup("Network error. Please try again!");
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === "Enter") handleSearch();
  };

  return (
    <>
      <div className="search-bg">
        <div className="glass-box">
          <h1 className="title">Weather</h1>
          <p className="subtitle">Search</p>

          <input
            type="text"
            className="input-box"
            placeholder="Enter city"
            value={city}
            onChange={(e) => {
              setCity(e.target.value);
              setErrorPopup("");
            }}
            onKeyDown={handleKeyPress}
          />

          {errorPopup && <p className="error-text  fs-5 fw-bold">{errorPopup}</p>}

          <button className="search-btn" onClick={handleSearch}>
            Search
          </button>
        </div>
      </div>

      {/* POPUP ERROR */}
      {/* {errorPopup && (
        <div className="popup-overlay">
          <div className="popup-box">
            <p>{errorPopup}</p>
            <button className="popup-btn" onClick={() => setErrorPopup("")}>
              OK
            </button>
          </div>
        </div>
      )} */}
    </>
  );
};

export default SearchWeather;



