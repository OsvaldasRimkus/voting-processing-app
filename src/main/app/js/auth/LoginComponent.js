const React = require('react');

function LoginComponent(props) {
    return (
        <div>
            <div className="container">
                <h1 id="login-header" style={{ textAlign: 'center' }}>PRISIJUNGTI</h1>
                <div className="col-md-4 col-md-offset-4">
                    <form className="" onSubmit={props.doLogin}>
                        {props.loginError}
                        <div className="form-group">
                            <label>Vardas</label>
                            <input
                                id="loging-input"
                                type="text"
                                className="form-control"
                                onChange={props.changeName}
                                value={props.username}/>
                        </div>
                        <div className="form-group">
                            <label>Slaptažodis</label>
                            <input
                                id="password-input"
                                type="password"
                                className="form-control"
                                onChange={props.changePassword}
                                value={props.password}
                            />
                        </div>
                        <button id="login-button" type="submit" className="btn btn-success">Prisijungti</button>
                    </form>
                </div>
            </div>
        </div>
    );
}

module.exports = LoginComponent;