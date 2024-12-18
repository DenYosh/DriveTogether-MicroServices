import React from "react";

const Footer = () => {
	return (
		<footer className="bg-red-500 text-black py-4">
			<div className="container mx-auto text-center">
				<p>
					&copy; {new Date().getFullYear()} DriveTogether. All rights
					reserved.
				</p>
			</div>
		</footer>
	);
};

export default Footer;
