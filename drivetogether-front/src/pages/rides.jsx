import React, { useEffect, useState } from "react";
import { ApiUrl, Bearertoken, User } from "../recoil/store";
import { useRecoilValue } from "recoil";
import { Link } from "react-router-dom";
import useShowNotification from "../component/notification";

const Rides = () => {
	const [rides, setRides] = useState([]);
	const [destinationFilter, setDestinationFilter] = useState("");
	const [completedFilter, setCompletedFilter] = useState("");
	const [ownRidesFilter, setOwnRidesFilter] = useState(false);
	const [bookedSeats, setBookedSeats] = useState(0);
	const [modalOpen, setModalOpen] = useState(false);
	const [selectedRide, setSelectedRide] = useState(null);
	const globalUser = useRecoilValue(User);
	const bearerToken = useRecoilValue(Bearertoken);
	const showNotification = useShowNotification();

	const fetchRides = async () => {
		try {
			const response = await fetch(`${ApiUrl}/rides`);
			const data = await response.json();
			setRides(data);
		} catch (error) {
			console.error("Error fetching rides:", error);
		}
	};

	useEffect(() => {
		fetchRides();
	}, []);

	const filteredRides = rides.filter((ride) => {
		return (
			(destinationFilter === "" ||
				ride.destination.toLowerCase().includes(destinationFilter)) &&
			(completedFilter === "" ||
				ride.completed.toString() === completedFilter) &&
			(!ownRidesFilter ||
				(globalUser && globalUser.id === ride.vehicle.owner.id))
		);
	});

	const makeBooking = () => {
		console.log("Making booking for ride:", selectedRide.id, globalUser);
		fetch(`${ApiUrl}/bookings`, {
			method: "POST",
			headers: {
				Authorization: `Bearer ${bearerToken}`,
				"Content-Type": "application/json",
			},
			body: JSON.stringify({
				userId: globalUser.id,
				rideId: selectedRide.id,
				seatsBooked: bookedSeats,
			}),
		})
			.then((response) => response.json())
			.then((data) => {
				if (data.error) {
					alert(data.error);
				} else {
					showNotification("Ride booked successfully!", "success");
					fetchRides();
					setModalOpen(false);
				}
			})
			.catch((error) => console.error("Error booking ride:", error));
	};

	const markAsCompleted = (rideId) => {
		fetch(`${ApiUrl}/rides/${rideId}/complete`, {
			method: "PUT",
			headers: {
				Authorization: `Bearer ${bearerToken}`,
				"Content-Type": "application/json",
			},
		})
			.then((response) => response.json())
			.then((data) => {
				if (data.error) {
					alert(data.error);
				} else {
					showNotification("Ride marked as completed!", "success");
					fetchRides();
				}
			})
			.catch((error) =>
				console.error("Error marking ride as completed:", error)
			);
	};

	return (
		<div className="my-6 text-white min-h-screen">
			<h1 className="text-3xl font-bold mb-6">Available Rides</h1>
			<div className="mb-6">
				<h2 className="text-2xl font-bold mb-2">Legend</h2>
				<ul>
					<li className="mb-1">
						<span className="inline-block w-4 h-4 bg-red-600 mr-2"></span>
						<span>Available ride</span>
					</li>
					<li className="mb-1">
						<span className="inline-block w-4 h-4 bg-red-800 mr-2"></span>
						<span>Completed ride</span>
					</li>
					<li className="mb-1">
						<span className="inline-block w-4 h-4 bg-gray-600 opacity-50 mr-2"></span>
						<span>No available seats</span>
					</li>
					<li className="mb-1">
						<span className="inline-block w-4 h-4 border-2 border-yellow-500 mr-2"></span>
						<span>Ride of the selected user</span>
					</li>
				</ul>
			</div>
			{!globalUser && bearerToken && (
				<div className="text-center">
					<p>
						Please select a{" "}
						<Link to={"/users"} className="underline">
							user
						</Link>{" "}
						to make a booking.
					</p>
				</div>
			)}
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
				{globalUser && (
					<label className="ml-4 inline-flex items-center">
						<input
							type="checkbox"
							checked={ownRidesFilter}
							onChange={(e) =>
								setOwnRidesFilter(e.target.checked)
							}
							className="form-checkbox h-5 w-5 text-red-600"
						/>
						<span className="ml-2">Show only my rides</span>
					</label>
				)}
			</div>
			{filteredRides.length === 0 ? (
				<div className="flex justify-center items-center h-64">
					<p className="text-2xl">No rides found</p>
				</div>
			) : (
				<div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
					{filteredRides.map((ride) => (
						<div
							key={ride.id}
							className={`border-2 p-6 rounded-lg shadow-lg transform transition duration-500 hover:scale-105 ${
								ride.completed ? "bg-red-800" : "bg-red-600"
							} ${
								ride.availableSeats <= 0 ? "bg-gray-600" : ""
							} ${
								globalUser &&
								globalUser.id === ride.vehicle.owner.id
									? "border-yellow-500"
									: ""
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
								{ride.availableSeats > 0 ? (
									<p className="text-gray-300">
										<span className="font-bold text-gray-200">
											Seats Available:
										</span>{" "}
										{ride.availableSeats}
									</p>
								) : (
									<p className="text-white font-bold">
										No seats available
									</p>
								)}
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
								{ride.availableSeats > 0 &&
									!ride.completed &&
									bearerToken &&
									globalUser &&
									globalUser.id !== ride.vehicle.owner.id && (
										<button
											className="bg-red-400 text-white p-2 rounded w-20 mt-4 border hover:bg-red-800 hover:text-gray-100"
											onClick={() => {
												setSelectedRide(ride);
												setModalOpen(true);
											}}
										>
											Book
										</button>
									)}
								{globalUser &&
									globalUser.id === ride.vehicle.owner.id &&
									!ride.completed && (
										<button
											className="bg-green-500 text-white p-2 rounded w-40 mt-4 border hover:bg-green-700 hover:text-gray-100"
											onClick={() =>
												markAsCompleted(ride.id)
											}
										>
											Mark as Completed
										</button>
									)}
							</div>
						</div>
					))}
				</div>
			)}

			{modalOpen && (
				<div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
					<div className="bg-white p-6 rounded-lg w-full md:w-1/2 lg:w-1/3">
						<h2 className="text-2xl font-bold mb-4 text-black">
							Book Ride
						</h2>
						<label className="block text-gray-900">
							Number of seats
						</label>
						<input
							type="number"
							placeholder="Number of seats"
							value={bookedSeats}
							onChange={(e) => setBookedSeats(e.target.value)}
							className="p-2 rounded border mb-4 w-full"
						/>
						<div className="flex justify-end space-x-4">
							<button
								className="bg-gray-500 text-white p-2 rounded"
								onClick={() => setModalOpen(false)}
							>
								Cancel
							</button>
							<button
								className="bg-red-500 text-white p-2 rounded disabled:bg-gray-500 disabled:cursor-not-allowed"
								onClick={makeBooking}
								disabled={
									bookedSeats <= 0 ||
									bookedSeats > selectedRide.availableSeats
								}
							>
								Book
							</button>
						</div>
					</div>
				</div>
			)}
		</div>
	);
};

export default Rides;
