import { atom } from "recoil";

export const ApiUrl =
	process.env.REACT_APP_API_GATEWAY_BASEURL || "http://localhost:8084";

export const RedirectUri =
	process.env.REACT_APP_REDIRECT_URI || "http://localhost:5173/callback";

export const Bearertoken = atom({
	key: "Bearertoken",
	default: null,
});
