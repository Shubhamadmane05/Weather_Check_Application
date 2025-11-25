

import { BrowserRouter, Routes, Route } from "react-router-dom";
import SearchWeather from "./components/SearchWeather";
import WeatherDetails from "./components/WeatherDetails";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<SearchWeather />} />
        <Route path="/weather/:city" element={<WeatherDetails />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
