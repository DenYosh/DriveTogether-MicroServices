import React, { useState } from "react";
import { useRecoilState, useSetRecoilState } from "recoil";
import { notificationState } from "../recoil/store";

export const Notification = () => {
	const [notification, setNotification] = useRecoilState(notificationState);

	if (!notification.message) return null;

	const typeClasses = {
		success: "bg-green-500 text-white",
		error: "bg-red-500 text-white",
		warning: "bg-yellow-500 text-black",
	};

	return (
		<div
			className={`fixed top-4 right-4 !z-50 p-4 rounded ${
				typeClasses[notification.type]
			}`}
		>
			{notification.message}
		</div>
	);
};

const useShowNotification = () => {
	const setNotification = useSetRecoilState(notificationState);

	const showNotification = (message, type = "success") => {
		setNotification({ message, type });
		setTimeout(() => setNotification({ message: "", type: "" }), 5000);
	};

	return showNotification;
};

export default useShowNotification;
