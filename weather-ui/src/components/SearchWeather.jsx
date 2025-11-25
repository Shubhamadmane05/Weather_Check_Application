import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./SearchWeather.css"; // 👈 Add this line for external CSS

const SearchWeather = () => {
  const [city, setCity] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSearch = () => {
    if (!city.trim()) {
      setError("Please enter a valid city name");
      return;
    }
    navigate(`/weather/${city.trim()}`);
  };

  const handleKeyPress = (e) => {
    if (e.key === "Enter") handleSearch();
  };

  return (
    <div className="weather-search-container">
      <div className="weather-card">
        <h2 className="weather-title">Weather Forecast</h2>

        <input
          type="text"
          className="weather-input"
          placeholder="Enter city..."
          value={city}
          onChange={(e) => {
            setCity(e.target.value);
            setError("");
          }}
          onKeyDown={handleKeyPress}
        />

        {error && <p className="error-text">{error}</p>}

        <button className="weather-btn" onClick={handleSearch}>
          Search
        </button>
      </div>
    </div>
  );
};

export default SearchWeather;





// import { useState } from "react";
// import { useNavigate } from "react-router-dom";

// const SearchWeather = () => {
//   const [city, setCity] = useState("");
//   const [error, setError] = useState("");
//   const navigate = useNavigate();

//   const handleSearch = () => {
//     if (!city.trim()) {
//       setError("Please enter a valid city name");
//       return;
//     }

//     navigate(`/weather/${city.trim()}`);
//   };

//   const handleKeyPress = (e) => {
//     if (e.key === "Enter") {
//       handleSearch();
//     }
//   };

//   return (
//     <div className="d-flex justify-content-center align-items-center vh-100 bg-primary text-white">
//       <div className="card p-5 bg-transparent border-0 text-center">
//         <h1 className="mb-4 fw-bold">Weather Forecast</h1>

//         <input
//           type="text"
//           className="form-control"
//           placeholder="Enter city name..."
//           value={city}
//           onChange={(e) => {
//             setCity(e.target.value);
//             setError("");
//           }}
//           onKeyDown={handleKeyPress}
//         />

//         {error && <p className="text-danger mt-2">{error}</p>}

//         <button className="btn btn-light mt-3 fw-bold w-100" onClick={handleSearch}>
//           Search
//         </button>
//       </div>
//     </div>
//   );
// };

// export default SearchWeather;
