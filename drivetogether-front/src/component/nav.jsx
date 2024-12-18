import React from "react";
import { Link } from "react-router-dom";

const Navbar = () => {
	return (
		<nav className="bg-red-500 text-black p-4">
			<div className="container mx-auto font-semibold">
				<ul className="flex justify-center space-x-8">
					<li>
						<Link to="/rides" className="hover:text-white">
							Rides
						</Link>
					</li>
					<li>
						<Link to="/profile" className="hover:text-white">
							Profile
						</Link>
					</li>
					<li>
						<Link to="/my-cars" className="hover:text-white">
							My Cars
						</Link>
					</li>
					<li>
						<Link to="/my-bookings" className="hover:text-white">
							My Bookings
						</Link>
					</li>
				</ul>
			</div>
		</nav>
	);
};

export default Navbar;
