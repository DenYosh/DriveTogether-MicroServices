import React from "react";
import { Link } from "react-router-dom";
import { useRecoilValue } from "recoil";
import { Bearertoken } from "../recoil/store";
import { useLocation } from "react-router-dom";

const Navbar = () => {
	const bearerToken = useRecoilValue(Bearertoken);
	const location = useLocation();

	const isActive = (path) => location.pathname === path;

	return (
		<nav className="bg-red-500 text-black p-4">
			<div className="container mx-auto font-semibold">
				<ul className="flex justify-center space-x-8">
					<li>
						<Link
							to="/rides"
							className={`hover:text-white ${
								isActive("/rides") ? "text-white" : ""
							}`}
						>
							Rides
						</Link>
					</li>
					{bearerToken ? (
						<>
							<li>
								<Link
									to="/users"
									className={`hover:text-white ${
										isActive("/users") ? "text-white" : ""
									}`}
								>
									Users
								</Link>
							</li>
							<li>
								<Link
									to="/profile"
									className={`hover:text-white ${
										isActive("/profile") ? "text-white" : ""
									}`}
								>
									Profile
								</Link>
							</li>
							<li>
								<Link
									to="/my-cars"
									className={`hover:text-white ${
										isActive("/my-cars") ? "text-white" : ""
									}`}
								>
									My Cars
								</Link>
							</li>
							<li>
								<Link
									to="/my-bookings"
									className={`hover:text-white ${
										isActive("/my-bookings")
											? "text-white"
											: ""
									}`}
								>
									My Bookings
								</Link>
							</li>
						</>
					) : (
						<>
							<li>
								<Link
									to="/"
									className={`hover:text-white ${
										isActive("/") ? "text-white" : ""
									}`}
								>
									Login
								</Link>
							</li>
						</>
					)}
				</ul>
			</div>
		</nav>
	);
};

export default Navbar;
