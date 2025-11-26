import React, { useEffect, useState } from "react";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";
import "./WeatherDetails.css";

const WeatherDetails = () => {
  const { city } = useParams();
  const navigate = useNavigate();

  const [forecast, setForecast] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchForecast();
  }, [city]);

  const fetchForecast = async () => {
    setLoading(true);
    try {
      const res = await axios.get(
        `http://localhost:8080/api/weather/forecast?city=${city}`
      );
      setForecast(res.data);
    } catch (err) {
      alert("City not found!");
      navigate("/");
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="text-center mt-5 text-white">Loading...</div>;
  if (!forecast) return null;

  // first element as "current"
  const current = forecast.daily[0];

  // convert UNIX → time AM/PM
  const formatTime = (timestamp) => {
    return new Date(timestamp * 1000).toLocaleTimeString("en-US", {
      hour: "2-digit",
      minute: "2-digit",
      hour12: true,
    });
  };

  // convert UNIX → dd/mm/yyyy
  const formatDate = (timestamp) => {
    return new Date(timestamp * 1000).toLocaleDateString("en-GB");
  };

  // weekday from dt
  const getWeekday = (timestamp) => {
    return new Date(timestamp * 1000).toLocaleDateString("en-US", {
      weekday: "short",
    });
  };

  return (
    <div className="weather-container">

      <button className="btn btn-light" onClick={() => navigate("/")}>
        ← Search Again
      </button>

      {/* CURRENT WEATHER */}
      <div className="glass-card mt-4">
        <h1>{forecast.city}, {forecast.country}</h1>
        <h1 className="temp-large">{Math.round(current.maxTemp)}°C</h1>
        <p>{current.description}</p>

        <div className="mt-3">
          <span>Min: {current.minTemp}°C</span> <br />
          <span>Max: {current.maxTemp}°C</span>
        </div>

        <img
          src={`https://openweathermap.org/img/wn/${current.icon}@2x.png`}
          alt="weather-icon"
        />
      </div>

      {/* 3-HOUR FORECAST LIST */}
      <div className="row mt-4 g-3">
        {forecast.daily.map((d, i) => (
          <div className="col-6 col-md-2" key={i}>
            <div className="daily-card">
              
              <h6>{getWeekday(d.dt)}</h6>
              <p className="date">{formatDate(d.dt)}</p>
              <p className="time">{formatTime(d.dt)}</p>

              <img
                src={`https://openweathermap.org/img/wn/${d.icon}.png`}
                alt="icon"
              />

              <p>{Math.round(d.maxTemp)}° / {Math.round(d.minTemp)}°</p>
              <small>{d.description}</small>
            </div>
          </div>
        ))}
      </div>

    </div>
  );
};

export default WeatherDetails;

