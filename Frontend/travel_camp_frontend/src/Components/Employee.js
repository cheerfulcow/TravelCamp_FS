class Employee extends React.Component{
    render() {
        return (
            <tr>
                <td>{this.props.employee.login}</td>
                <td>{this.props.employee.password}</td>
                <td>{this.props.employee.role}</td>
            </tr>
        )
    }
}