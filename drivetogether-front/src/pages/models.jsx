import React, { useState, useEffect } from "react";
import { ApiUrl, Bearertoken } from "../recoil/store";
import { useRecoilValue } from "recoil";

const Models = () => {
	const [models, setModels] = useState([]);
	const [newModel, setNewModel] = useState("");
	const bearerToken = useRecoilValue(Bearertoken);

	useEffect(() => {
		fetch(ApiUrl + "/vehicles/models", {
			headers: {
				Authorization: `Bearer ${bearerToken}`,
			},
		})
			.then((response) => response.json())
			.then((data) => setModels(data))
			.catch((error) => console.error("Error fetching models:", error));
	}, []);

	const handleAddModel = () => {
		if (newModel.trim() === "") return;

		fetch(ApiUrl + "/vehicles/models", {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
				Authorization: `Bearer ${bearerToken}`,
			},
			body: JSON.stringify({ modelName: newModel }),
		})
			.then((response) => response.json())
			.then((data) => setModels([...models, data]))
			.catch((error) => console.error("Error adding model:", error));

		setNewModel("");
	};

	return (
		<div className="my-6 text-white">
			<h1 className="text-4xl font-bold mb-4">Vehicle Models</h1>
			<div className="bg-red-600 p-4 rounded-lg shadow-lg">
				<div className="mb-4">
					<input
						type="text"
						value={newModel}
						onChange={(e) => setNewModel(e.target.value)}
						className="border border-gray-300 p-2 rounded mr-2"
						placeholder="Add new model"
					/>
					<button
						onClick={handleAddModel}
						className={`bg-red-600 text-white p-2 rounded hover:bg-red-700 disabled:bg-gray-500 disabled:cursor-not-allowed`}
						disabled={newModel.trim() === ""}
					>
						Add Model
					</button>
				</div>
				<div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
					{models.map((model, index) => (
						<div
							key={index}
							className="bg-gray-800 text-white p-4 rounded shadow"
						>
							<h2 className="text-xl font-semibold">
								{model.modelName}
							</h2>
						</div>
					))}
				</div>
			</div>
		</div>
	);
};

export default Models;
