import React from "react";
import Login from "../component/login";
import { useRecoilValue } from "recoil";
import { ApiUrl, Bearertoken } from "../recoil/store";

const Home = () => {
	const bearerToken = useRecoilValue(Bearertoken);

	return (
		<div className="flex flex-col items-center justify-center my-8">
			<h1 className="text-4xl font-bold text-blue-300 mb-4">
				Welcome to DriveTogether
			</h1>
			<p className="text-lg text-gray-100 mb-6">
				Your one-stop solution for carpooling and ride-sharing.
			</p>
			{!bearerToken ? (
				<>
					<p className="text-md text-red-300 mb-4">
						Please log in to continue.
					</p>
					<Login />
				</>
			) : (
				<p className="text-md text-green-00">You are logged in.</p>
			)}
		</div>
	);
};

export default Home;
