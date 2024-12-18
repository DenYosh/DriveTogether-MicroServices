import { RedirectUri } from "../recoil/store";

function Login() {
	const clientId = process.env.REACT_APP_CLIENT_ID;
	const redirectUri = RedirectUri;
	const authUrl = process.env.REACT_APP_AUTH_URL;
	const scope = process.env.REACT_APP_SCOPE;

	function redirectToAuth() {
		const url = `${authUrl}?response_type=code&client_id=${clientId}&redirect_uri=${encodeURIComponent(
			redirectUri
		)}&scope=${encodeURIComponent(scope)}`;
		window.location.href = url;
	}

	return (
		<button
			onClick={redirectToAuth}
			className="bg-red-500 text-gray-200 border-none py-2 px-4 text-lg rounded-md cursor-pointer transition-transform duration-300 hover:bg-red-700 hover:scale-105"
		>
			Login with Google
		</button>
	);
}

export default Login;
