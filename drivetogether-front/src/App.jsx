import "./App.css";
import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Callback from "./pages/callback";
import Home from "./pages/home";

function App() {
	return (
		<>
			<Router>
				<Routes>
					<Route path="/" element={<Home />} />
					<Route path="/callback" element={<Callback />} />
				</Routes>
			</Router>
		</>
	);
}

export default App;
