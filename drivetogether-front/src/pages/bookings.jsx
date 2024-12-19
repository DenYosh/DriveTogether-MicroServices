import React, { useEffect, useState } from "react";
import { useRecoilValue } from "recoil";
import { ApiUrl, Bearertoken, User } from "../recoil/store";
import useShowNotification from "../component/notification";

const Bookings = () => {
	const [bookings, setBookings] = useState([]);
	const globalUser = useRecoilValue(User);
	const bearerToken = useRecoilValue(Bearertoken);
	const showNotification = useShowNotification();

	useEffect(() => {
		const fetchBookings = async () => {
			if (!globalUser) {
				return;
			}
			try {
				const response = await fetch(
					ApiUrl + "/bookings/user/" + globalUser.id,
					{
						headers: {
							Authorization: `Bearer ${bearerToken}`,
						},
					}
				);
				const data = await response.json();
				setBookings(data);
			} catch (error) {
				console.error("Error fetching bookings:", error);
			}
		};

		fetchBookings();
	}, [globalUser, bearerToken]);

	const handleDelete = async (bookingId) => {
		try {
			const response = await fetch(ApiUrl + "/bookings/" + bookingId, {
				method: "DELETE",
				headers: {
					Authorization: `Bearer ${bearerToken}`,
				},
			});
			if (response.ok) {
				showNotification("Booking cancelled successfully", "success");
				setBookings(
					bookings.filter((booking) => booking.id !== bookingId)
				);
			} else {
				console.error("Failed to delete booking");
			}
		} catch (error) {
			console.error("Error deleting booking:", error);
		}
	};

	return (
		<div className="my-6 text-white">
			<h1 className="text-4xl font-bold mb-4">My Bookings</h1>
			<div className="bg-red-600 p-6 rounded-lg shadow-2xl">
				{!globalUser && (
					<p className="text-center">
						Please log in to view your bookings.
					</p>
				)}
				{bookings.length === 0 ? (
					<p className="text-center">No bookings found.</p>
				) : (
					<ul className="space-y-6">
						{bookings.map((booking) => (
							<li
								key={booking.id}
								className="bg-gray-800 p-6 border rounded-lg"
							>
								<div className="flex justify-between">
									<p className="text-2xl font-semibold mb-2">
										Ride from {booking.ride.source} to{" "}
										{booking.ride.destination}
									</p>
									{!booking.ride.completed ? (
										<button
											onClick={() =>
												handleDelete(booking.id)
											}
											className="bg-red-400 p-2 rounded border-2"
										>
											Cancel Booking
										</button>
									) : null}
								</div>

								<p className="mb-1">
									Seats Booked: {booking.seatsBooked}
								</p>
								<p className="mb-1">
									Start Time:{" "}
									{new Date(
										booking.ride.startTime
									).toLocaleString()}
								</p>
								<p className="mb-1">
									Booking Time:{" "}
									{new Date(
										booking.bookingTime
									).toLocaleString()}
								</p>
								<p>
									Ride completed:{" "}
									<span
										className={
											booking.ride.completed
												? "text-green-500"
												: "text-red-500"
										}
									>
										{booking.ride.completed ? "Yes" : "No"}
									</span>
								</p>
							</li>
						))}
					</ul>
				)}
			</div>
		</div>
	);
};

export default Bookings;
