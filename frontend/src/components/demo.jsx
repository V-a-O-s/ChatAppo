import React from 'react';

const Person = ({person}) => {
    const hobbies = person.hobbies.map(hob => <li>{hob}</li>)
    return <>
        <p>Name: {person.name}</p>
        <p>Hobbies: {hobbies}</p>
    </>
}

function Demo({p1,p2,fun,person}) {
    return <>
        <h1>{ p1 }</h1>
        <Person person={person} />
        <button onClick={fun}>{ p2 }</button>
    </>
}

export default Demo