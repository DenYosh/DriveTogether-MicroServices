import "./App.css";
import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Callback from "./pages/callback";
import Home from "./pages/home";
import Navbar from "./component/nav";
import Footer from "./component/footer";
import Rides from "./pages/rides";
import Profile from "./pages/profile";
import Users from "./pages/users";
import { useRecoilValue } from "recoil";
import { Bearertoken } from "./recoil/store";
import Models from "./pages/models";
import Bookings from "./pages/bookings";
import { Notification } from "./component/notification";

function App() {
	const bearerToken = useRecoilValue(Bearertoken);

	return (
		<>
			<Router>
				<div className="flex flex-col min-h-screen">
					<Notification />
					<Navbar />
					<div className="container mx-auto flex-grow">
						<Routes>
							<Route path="/" element={<Home />} />
							<Route path="/callback" element={<Callback />} />
							<Route path="/rides" element={<Rides />} />

							{bearerToken && (
								<>
									<Route path="/users" element={<Users />} />
									<Route
										path="/profile"
										element={<Profile />}
									/>
									<Route
										path="/models"
										element={<Models />}
									/>
									<Route
										path="/my-bookings"
										element={<Bookings />}
									/>
								</>
							)}
						</Routes>
					</div>
					<Footer />
				</div>
			</Router>
		</>
	);
}

export default App;
