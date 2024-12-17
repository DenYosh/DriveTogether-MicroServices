function Login() {
	const clientId = process.env.REACT_APP_CLIENT_ID;
	const redirectUri = process.env.REACT_APP_REDIRECT_URI;
	const authUrl = process.env.REACT_APP_AUTH_URL;
	const scope = process.env.REACT_APP_SCOPE;

	function redirectToAuth() {
		const url = `${authUrl}?response_type=code&client_id=${clientId}&redirect_uri=${encodeURIComponent(
			redirectUri
		)}&scope=${encodeURIComponent(scope)}`;
		window.location.href = url;
	}

	return <button onClick={redirectToAuth}>Login with Google</button>;
}

export default Login;
