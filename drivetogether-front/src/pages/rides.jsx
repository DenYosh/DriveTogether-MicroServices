import React, { useEffect, useState } from "react";
import { ApiUrl } from "../recoil/store";

const Rides = () => {
	const [rides, setRides] = useState([]);
	const [destinationFilter, setDestinationFilter] = useState("");
	const [completedFilter, setCompletedFilter] = useState("");

	useEffect(() => {
		fetch(`${ApiUrl}/rides`)
			.then((response) => response.json())
			.then((data) => setRides(data))
			.catch((error) => console.error("Error fetching rides:", error));
	}, []);

	const filteredRides = rides.filter((ride) => {
		return (
			(destinationFilter === "" ||
				ride.destination.includes(destinationFilter)) &&
			(completedFilter === "" ||
				ride.completed.toString() === completedFilter)
		);
	});

	return (
		<div className="my-6 text-white min-h-screen">
			<h1 className="text-3xl font-bold mb-6">Available Rides</h1>
			<div className="mb-6">
				<input
					type="text"
					placeholder="Filter by destination"
					value={destinationFilter}
					onChange={(e) => setDestinationFilter(e.target.value)}
					className="p-2 rounded border"
				/>
				<select
					value={completedFilter}
					onChange={(e) => setCompletedFilter(e.target.value)}
					className="p-2 rounded border ml-4"
				>
					<option value="">All</option>
					<option value="true">Completed</option>
					<option value="false">Not Completed</option>
				</select>
			</div>
			{filteredRides.length === 0 ? (
				<div className="flex justify-center items-center min-h-screen">
					<div className="loader ease-linear rounded-full border-8 border-t-8 border-gray-200 h-32 w-32"></div>
				</div>
			) : (
				<div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
					{filteredRides.map((ride) => (
						<div
							key={ride.id}
							className={`border p-6 rounded-lg shadow-lg transform transition duration-500 hover:scale-105 ${
								ride.completed ? "bg-red-800" : "bg-red-600"
							}`}
						>
							<h2 className="text-2xl font-semibold mb-2 text-gray-100">
								{ride.destination}{" "}
								<span className="italic font-light text-lg text-gray-300">
									({ride.source})
								</span>
							</h2>
							<div className="flex flex-col space-y-1">
								<p className="text-gray-300">
									<span className="font-bold text-gray-200">
										Driver:
									</span>{" "}
									{ride.vehicle.owner.name}
								</p>
								<p className="text-gray-300">
									<span className="font-bold text-gray-200">
										Vehicle:
									</span>{" "}
									{ride.vehicle.make} {ride.vehicle.modelName}
								</p>
								<p className="text-gray-300">
									<span className="font-bold text-gray-200">
										License Plate:
									</span>{" "}
									{ride.vehicle.licensePlate}
								</p>
								<p className="text-gray-300">
									<span className="font-bold text-gray-200">
										Seats Available:
									</span>{" "}
									{ride.availableSeats}
								</p>
								<p className="text-gray-300">
									<span className="font-bold text-gray-200">
										Departure Time:
									</span>{" "}
									{new Date(ride.startTime).toLocaleString()}
								</p>
								<p className="text-gray-300">
									<span className="font-bold text-gray-200">
										Arrival Time:
									</span>{" "}
									{new Date(ride.endTime).toLocaleString()}
								</p>
								{ride.completed && (
									<p className="text-green-500 font-bold mt-2">
										Completed
									</p>
								)}
							</div>
						</div>
					))}
				</div>
			)}
		</div>
	);
};

export default Rides;
