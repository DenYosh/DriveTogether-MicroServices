import React, { useState, useEffect } from "react";
import { ApiUrl, Bearertoken, User } from "../recoil/store";
import { useRecoilValue } from "recoil";
import useShowNotification from "../component/notification";

const Profile = () => {
	const [user, setUser] = useState(null);
	const [vehicleModels, setVehicleModels] = useState([]);
	const [newVehicle, setNewVehicle] = useState({
		licensePlate: "",
		make: "",
		modelId: "",
		capacity: "",
	});
	const globalUser = useRecoilValue(User);
	const bearerToken = useRecoilValue(Bearertoken);

	const [modelOpen, setModelOpen] = useState(false);
	const [newRide, setNewRide] = useState({
		carId: "",
		source: "",
		destination: "",
		startTime: "",
		endTime: "",
		availableSeats: "",
	});

	const showNotification = useShowNotification();

	useEffect(() => {
		const fetchUserData = async () => {
			if (!globalUser) {
				return;
			}
			try {
				const response = await fetch(
					ApiUrl + "/users/" + globalUser.id,
					{
						headers: {
							Authorization: `Bearer ${bearerToken}`,
						},
					}
				);
				const data = await response.json();
				setUser(data);
			} catch (error) {
				console.error("Error fetching user data:", error);
			}
		};

		const fetchVehicleModels = async () => {
			try {
				const response = await fetch(ApiUrl + "/vehicles/models", {
					headers: {
						Authorization: `Bearer ${bearerToken}`,
					},
				});
				const data = await response.json();
				setVehicleModels(data);
			} catch (error) {
				console.error("Error fetching vehicle models:", error);
			}
		};

		fetchUserData();
		fetchVehicleModels();
	}, [globalUser, bearerToken]);

	const handleInputChange = (e) => {
		const { name, value } = e.target;
		setNewVehicle((prev) => ({ ...prev, [name]: value }));
	};

	const handleAddVehicle = async () => {
		try {
			const response = await fetch(ApiUrl + "/vehicles", {
				method: "POST",
				headers: {
					"Content-Type": "application/json",
					Authorization: `Bearer ${bearerToken}`,
				},
				body: JSON.stringify({
					ownerId: globalUser.id,
					...newVehicle,
				}),
			});
			const data = await response.json();
			setUser((prev) => ({
				...prev,
				vehicles: [...prev.vehicles, data],
			}));
			setNewVehicle({
				licensePlate: "",
				make: "",
				modelId: "",
				capacity: "",
			});
			showNotification("Vehicle added successfully", "success");
		} catch (error) {
			console.error("Error adding vehicle:", error);
		}
	};

	const handleRideInputChange = (e) => {
		setModelOpen(true);
		const { name, value } = e.target;
		setNewRide((prev) => ({ ...prev, [name]: value }));
	};

	const handleAddRide = async () => {
		try {
			const response = await fetch(ApiUrl + "/rides", {
				method: "POST",
				headers: {
					"Content-Type": "application/json",
					Authorization: `Bearer ${bearerToken}`,
				},
				body: JSON.stringify(newRide),
			});
			const data = await response.json();
			setNewRide({
				carId: "",
				source: "",
				destination: "",
				startTime: "",
				endTime: "",
				availableSeats: "",
			});
			setModelOpen(false);
			showNotification("Ride added successfully", "success");
		} catch (error) {
			console.error("Error adding ride:", error);
		}
	};

	if (!globalUser) {
		return <div>Please select a user to view their profile.</div>;
	}

	if (!user) {
		return <div>Loading...</div>;
	}

	return (
		<div className="my-6 text-white">
			<h1 className="text-4xl font-bold mb-4">Profile Page</h1>
			<div className="bg-red-600 p-4 rounded-lg shadow-lg">
				<div className="flex justify-between">
					<h2 className="text-2xl font-semibold mb-2">
						Name: {user.name}
					</h2>
					<button
						className="bg-red-400 p-2 rounded border-2"
						onClick={() => setModelOpen(true)}
					>
						Create Ride
					</button>
				</div>

				<p className="text-lg mb-2">Email: {user.email}</p>
				<p className="text-lg mb-2">Phone: {user.phoneNumber}</p>
				<p className="text-lg mb-2">Address: {user.address}</p>
				<h3 className="text-xl font-semibold mb-2">Vehicles:</h3>
				<ul>
					{user.vehicles.length === 0 && (
						<p className="text-lg">No vehicles found.</p>
					)}
					{user.vehicles.map((vehicle) => (
						<li key={vehicle.id} className="mb-2">
							<div className="bg-gray-800 p-4 rounded-lg">
								<p className="text-lg font-medium">
									{vehicle.make} {vehicle.modelName} (License
									Plate: {vehicle.licensePlate}, Capacity:{" "}
									{vehicle.capacity})
								</p>
							</div>
						</li>
					))}
				</ul>
				<div className="p-2 bg-red-400 my-4 rounded-lg">
					<h3 className="text-xl font-semibold mb-2">Add Vehicle:</h3>
					<div className="flex flex-wrap space-x-5 p-4 rounded-lg">
						<input
							type="text"
							name="licensePlate"
							placeholder="License Plate"
							value={newVehicle.licensePlate}
							onChange={handleInputChange}
							className="mb-2 p-2 rounded"
						/>
						<input
							type="text"
							name="make"
							placeholder="Make"
							value={newVehicle.make}
							onChange={handleInputChange}
							className="mb-2 p-2 rounded"
						/>
						<select
							name="modelId"
							value={newVehicle.modelId}
							onChange={handleInputChange}
							className="mb-2 p-2 rounded"
						>
							<option value="">Select Model</option>
							{vehicleModels.map((model) => (
								<option key={model.id} value={model.id}>
									{model.modelName}
								</option>
							))}
						</select>
						<input
							type="number"
							name="capacity"
							placeholder="Capacity"
							value={newVehicle.capacity}
							onChange={handleInputChange}
							className="mb-2 p-2 rounded"
						/>
						<button
							onClick={handleAddVehicle}
							className="bg-red-500 p-2 rounded disabled:bg-gray-500 disabled:cursor-not-allowed cursor-pointer"
							{...(!newVehicle.licensePlate ||
							!newVehicle.make ||
							!newVehicle.modelId ||
							!newVehicle.capacity
								? { disabled: true }
								: {})}
						>
							Add Vehicle
						</button>
					</div>
				</div>
			</div>
			{modelOpen && (
				<div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
					<div className="bg-white p-6 rounded-lg shadow-lg w-full md:w-1/2 lg:w-1/3">
						<h3 className="text-xl font-semibold mb-2 text-black">
							Add Ride:
						</h3>
						<div className="flex flex-col space-y-5 p-4 rounded-lg">
							<select
								name="carId"
								value={newRide.carId}
								onChange={handleRideInputChange}
								className="mb-2 p-2 rounded"
							>
								<option value="">Select Car</option>
								{user.vehicles.map((vehicle) => (
									<option key={vehicle.id} value={vehicle.id}>
										{vehicle.make} {vehicle.modelName}
									</option>
								))}
							</select>
							<input
								type="text"
								name="source"
								placeholder="Source"
								value={newRide.source}
								onChange={handleRideInputChange}
								className="mb-2 p-2 rounded"
							/>
							<input
								type="text"
								name="destination"
								placeholder="Destination"
								value={newRide.destination}
								onChange={handleRideInputChange}
								className="mb-2 p-2 rounded"
							/>
							<input
								type="datetime-local"
								name="startTime"
								value={newRide.startTime}
								onChange={handleRideInputChange}
								className="mb-2 p-2 rounded"
							/>
							<input
								type="datetime-local"
								name="endTime"
								value={newRide.endTime}
								onChange={handleRideInputChange}
								className="mb-2 p-2 rounded"
							/>
							<input
								type="number"
								name="availableSeats"
								placeholder="Available Seats"
								value={newRide.availableSeats}
								onChange={handleRideInputChange}
								className="mb-2 p-2 rounded"
							/>
							<button
								onClick={handleAddRide}
								className="bg-red-500 p-2 rounded disabled:bg-gray-500 disabled:cursor-not-allowed cursor-pointer"
								{...(!newRide.carId ||
								!newRide.source ||
								!newRide.destination ||
								!newRide.startTime ||
								!newRide.endTime ||
								!newRide.availableSeats
									? { disabled: true }
									: {})}
							>
								Add Ride
							</button>
						</div>
						<button
							onClick={() => setModelOpen(false)}
							className="mt-4 bg-gray-500 p-2 rounded"
						>
							Close
						</button>
					</div>
				</div>
			)}
		</div>
	);
};

export default Profile;
