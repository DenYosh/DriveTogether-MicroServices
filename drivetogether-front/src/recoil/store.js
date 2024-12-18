import { atom } from "recoil";

export const ApiUrl =
	process.env.NODE_ENV == "production"
		? "api-gateway:8084"
		: "http://localhost:8084";

export const RedirectUri =
	process.env.NODE_ENV == "production"
		? "http://localhost/callback"
		: "http://localhost:5173/callback";

export const Bearertoken = atom({
	key: "Bearertoken",
	default: null,
});
