import { atom } from "recoil";

export const ApiUrl =
	process.env.REACT_APP_API_GATEWAY_BASEURL || "http://localhost:8084";

export const Bearertoken = atom({
	key: "Bearertoken",
	default: null,
});
