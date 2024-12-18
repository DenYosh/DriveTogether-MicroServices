import React, { useEffect, useState } from "react";
import { ApiUrl, Bearertoken } from "../recoil/store";
import { useRecoilValue } from "recoil";

const Rides = () => {
	const [rides, setRides] = useState([]);
	const bearerToken = useRecoilValue(Bearertoken);

	useEffect(() => {
		fetch(`${ApiUrl}/rides`, {
			headers: {
				Authorization: `Bearer ${bearerToken}`,
			},
		})
			.then((response) => response.json())
			.then((data) => setRides(data))
			.catch((error) => console.error("Error fetching rides:", error));
	}, []);

	return (
		<div className="my-6 text-white min-h-screen">
			<h1 className="text-3xl font-bold mb-6">Available Rides</h1>
			{rides.length === 0 ? (
				<div className="flex justify-center items-center min-h-screen">
					<div className="loader ease-linear rounded-full border-8 border-t-8 border-gray-200 h-32 w-32"></div>
				</div>
			) : (
				<div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
					{rides.map((ride) => (
						<div
							key={ride.id}
							className={`border p-6 rounded-lg shadow-lg ${
								ride.completed ? "bg-gray-700" : "bg-gray-800"
							}`}
						>
							<h2 className="text-2xl font-semibold mb-2">
								{ride.destination}
							</h2>
							<p className="text-gray-400 mb-1">
								Driver: {ride.vehicle.owner.name}
							</p>
							<p className="text-gray-400 mb-1">
								Vehicle: {ride.vehicle.make}{" "}
								{ride.vehicle.modelName}
							</p>
							<p className="text-gray-400 mb-1">
								License Plate: {ride.vehicle.licensePlate}
							</p>
							<p className="text-gray-400 mb-1">
								Seats Available: {ride.availableSeats}
							</p>
							<p className="text-gray-400 mb-1">
								Departure Time:{" "}
								{new Date(ride.startTime).toLocaleString()}
							</p>
							<p className="text-gray-400 mb-1">
								Arrival Time:{" "}
								{new Date(ride.endTime).toLocaleString()}
							</p>
							{ride.completed && (
								<p className="text-green-500 font-bold mt-2">
									Completed
								</p>
							)}
						</div>
					))}
				</div>
			)}
		</div>
	);
};

export default Rides;
