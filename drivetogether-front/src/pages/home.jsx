import React from "react";
import Login from "../component/login";
import { useRecoilValue } from "recoil";
import { ApiUrl, Bearertoken } from "../recoil/store";

const Home = () => {
	const bearerToken = useRecoilValue(Bearertoken);

	return (
		<div>
			<h1>Welcome to DriveTogether</h1>
			<p>Your one-stop solution for carpooling and ride-sharing.</p>
			{!bearerToken ? (
				<>
					<p>Please log in to continue.</p>
					<Login />
				</>
			) : (
				<p>You are logged in.</p>
			)}
		</div>
	);
};

export default Home;
