import React, { useEffect, useState } from "react";
import { ApiUrl, Bearertoken, User } from "../recoil/store";
import { useRecoilState, useRecoilValue } from "recoil";
import useShowNotification from "../component/notification";

const Users = () => {
	const [users, setUsers] = useState([]);
	const [selectedUser, setSelectedUser] = useState(null);
	const [isModalOpen, setIsModalOpen] = useState(false);
	const [formData, setFormData] = useState({
		name: "",
		email: "",
		phoneNumber: "",
		address: "",
	});
	const bearerToken = useRecoilValue(Bearertoken);
	const [globalUser, setGlobalUser] = useRecoilState(User);

	const showNotification = useShowNotification();

	useEffect(() => {
		fetch(`${ApiUrl}/users`, {
			headers: {
				Authorization: `Bearer ${bearerToken}`,
			},
		})
			.then((response) => response.json())
			.then((data) => setUsers(data))
			.catch((error) => console.error("Error fetching users:", error));
	}, []);

	const handleOpenModal = (user = null) => {
		setSelectedUser(user);
		setFormData(
			user || { name: "", email: "", phoneNumber: "", address: "" }
		);
		setIsModalOpen(true);
	};

	const handleCloseModal = () => {
		setIsModalOpen(false);
		setSelectedUser(null);
	};

	const handleChange = (e) => {
		const { name, value } = e.target;
		setFormData({ ...formData, [name]: value });
	};

	const handleSubmit = (e) => {
		e.preventDefault();
		const method = selectedUser ? "PUT" : "POST";
		const url = selectedUser
			? `${ApiUrl}/users/${selectedUser.id}`
			: `${ApiUrl}/users`;

		fetch(url, {
			method,
			headers: {
				"Content-Type": "application/json",
				Authorization: `Bearer ${bearerToken}`,
			},
			body: JSON.stringify(formData),
		})
			.then((response) => response.json())
			.then((data) => {
				if (selectedUser) {
					setUsers(
						users.map((user) => (user.id === data.id ? data : user))
					);
				} else {
					setUsers([...users, data]);
				}
				handleCloseModal();
				showNotification(
					selectedUser
						? "User updated successfully"
						: "User added successfully",
					"success"
				);
			})
			.catch((error) => console.error("Error saving user:", error));
	};

	return (
		<div className="my-6 text-white">
			<h1 className="text-4xl font-bold mb-6">All users</h1>
			<div className="bg-red-600 p-4 rounded-lg shadow-lg">
				<button
					className="mb-4 px-4 py-2 bg-red-400 border-2 rounded"
					onClick={() => handleOpenModal()}
				>
					Add User
				</button>
				{users.length === 0 ? (
					<div className="flex justify-center items-center min-h-screen">
						<div className="loader ease-linear rounded-full border-8 border-t-8 border-gray-200 max-h-40"></div>
					</div>
				) : (
					<div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
						{users.map((user) => (
							<div
								key={user.id}
								className={`border p-6 rounded-lg shadow-lg bg-gray-800 transition-all duration-300 cursor-pointer hover:bg-gray-700 hover:scale-105 ${
									globalUser && globalUser.id === user.id
										? "border-red-500 scale-105"
										: ""
								}`}
								onClick={() => setGlobalUser(user)}
							>
								<div className="flex justify-between">
									<h2 className="text-2xl font-semibold mb-2 text-red-500">
										{user.name}
									</h2>
									{globalUser &&
										globalUser.id === user.id && (
											<svg
												xmlns="http://www.w3.org/2000/svg"
												className="h-6 w-6 text-red-500"
												fill="none"
												viewBox="0 0 24 24"
												stroke="currentColor"
												onClick={(e) => {
													e.stopPropagation();
													setGlobalUser(null);
												}}
											>
												<path
													strokeLinecap="round"
													strokeLinejoin="round"
													strokeWidth={2}
													d="M6 18L18 6M6 6l12 12"
												/>
											</svg>
										)}
								</div>

								<p className="text-gray-400 mb-1">
									Email: {user.email}
								</p>
								<p className="text-gray-400 mb-1">
									Phone: {user.phoneNumber}
								</p>
								<p className="text-gray-400 mb-1">
									Address: {user.address}
								</p>
								<button
									className="px-4 py-2 bg-red-500 text-white rounded w-20 mt-4"
									onClick={(e) => {
										e.stopPropagation();
										handleOpenModal(user);
									}}
								>
									Edit
								</button>
							</div>
						))}
					</div>
				)}

				{isModalOpen && (
					<div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
						<div className="bg-white p-6 rounded-lg shadow-lg w-full md:w-1/2 lg:w-1/3">
							<h2 className="text-2xl font-bold mb-4 text-black">
								{selectedUser ? "Edit User" : "Add User"}
							</h2>
							<form onSubmit={handleSubmit}>
								<div className="mb-4">
									<label className="block text-gray-900">
										Name
									</label>
									<input
										type="text"
										name="name"
										value={formData.name}
										onChange={handleChange}
										className="w-full px-3 py-2 border rounded"
									/>
								</div>
								<div className="mb-4">
									<label className="block text-gray-900">
										Email
									</label>
									<input
										type="email"
										name="email"
										value={formData.email}
										onChange={handleChange}
										className="w-full px-3 py-2 border rounded"
									/>
								</div>
								<div className="mb-4">
									<label className="block text-gray-900">
										Phone Number
									</label>
									<input
										type="text"
										name="phoneNumber"
										value={formData.phoneNumber}
										onChange={handleChange}
										className="w-full px-3 py-2 border rounded"
									/>
								</div>
								<div className="mb-4">
									<label className="block text-gray-900">
										Address
									</label>
									<input
										type="text"
										name="address"
										value={formData.address}
										onChange={handleChange}
										className="w-full px-3 py-2 border rounded"
									/>
								</div>
								<div className="flex justify-between">
									<button
										type="button"
										className="mr-4 px-4 py-2 bg-gray-500 text-white rounded"
										onClick={handleCloseModal}
									>
										Cancel
									</button>
									<button
										type="submit"
										className="px-4 py-2 bg-red-500 text-white rounded disabled:cursor-not-allowed disabled:bg-red-300"
										{...(!formData.name ||
										!formData.email ||
										!formData.phoneNumber ||
										!formData.address
											? { disabled: true }
											: {})}
									>
										{selectedUser
											? "Save Changes"
											: "Add User"}
									</button>
								</div>
							</form>
						</div>
					</div>
				)}
			</div>
		</div>
	);
};

export default Users;
