import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useSetRecoilState } from "recoil";
import { Bearertoken, RedirectUri } from "../recoil/store";

function Callback() {
	const navigate = useNavigate();
	const setBearerToken = useSetRecoilState(Bearertoken);

	useEffect(() => {
		const queryParams = new URLSearchParams(window.location.search);
		const authorizationCode = queryParams.get("code");

		if (authorizationCode) {
			exchangeCodeForToken(authorizationCode);
		}
	}, []);

	const exchangeCodeForToken = async (code) => {
		const body = new URLSearchParams();
		body.append("grant_type", "authorization_code");
		body.append("code", code);
		body.append("redirect_uri", RedirectUri);
		body.append("client_id", process.env.REACT_APP_CLIENT_ID);
		body.append("client_secret", process.env.REACT_APP_CLIENT_SECRET);

		const response = await fetch(process.env.REACT_APP_TOKEN_URL, {
			method: "POST",
			headers: {
				"Content-Type": "application/x-www-form-urlencoded",
			},
			body: body.toString(),
		});

		const data = await response.json();
		setBearerToken(data.id_token);
		navigate("/");
	};

	return <div>Verwerken...</div>;
}

export default Callback;
