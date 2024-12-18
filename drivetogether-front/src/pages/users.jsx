import React, { useEffect, useState } from "react";
import { ApiUrl, Bearertoken } from "../recoil/store";
import { useRecoilValue } from "recoil";

const Users = () => {
	const [users, setUsers] = useState([]);
	const bearerToken = useRecoilValue(Bearertoken);

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

	return (
		<div className="my-6 text-white">
			<h1 className="text-3xl font-bold mb-6 text-red-500">Users</h1>
			{users.length === 0 ? (
				<div className="flex justify-center items-center min-h-screen">
					<div className="loader ease-linear rounded-full border-8 border-t-8 border-gray-200 max-h-40"></div>
				</div>
			) : (
				<div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
					{users.map((user) => (
						<div
							key={user.id}
							className="border p-6 rounded-lg shadow-lg bg-gray-800"
						>
							<h2 className="text-2xl font-semibold mb-2 text-red-500">
								{user.name}
							</h2>
							<p className="text-gray-400 mb-1">
								Email: {user.email}
							</p>
							<p className="text-gray-400 mb-1">
								Phone: {user.phoneNumber}
							</p>
							<p className="text-gray-400 mb-1">
								Address: {user.address}
							</p>
						</div>
					))}
				</div>
			)}
		</div>
	);
};

export default Users;
